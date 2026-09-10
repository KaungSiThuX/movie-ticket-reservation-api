package com.kst.movie_ticket_reservation.feat.auth.user.service.impl;

import com.kst.movie_ticket_reservation.feat.auth.dto.res.CommonAuthResDto;
import com.kst.movie_ticket_reservation.feat.auth.user.dto.req.*;
import com.kst.movie_ticket_reservation.feat.auth.user.dto.res.*;
import com.kst.movie_ticket_reservation.feat.auth.user.entity.User;
import com.kst.movie_ticket_reservation.feat.auth.user.payload.TemporaryStoreOtpPayload;
import com.kst.movie_ticket_reservation.feat.auth.user.repository.UserRepository;
import com.kst.movie_ticket_reservation.feat.auth.user.service.UserService;
import com.kst.movie_ticket_reservation.integration.google.oauth.service.GoogleOAuthService;
import com.kst.movie_ticket_reservation.integration.redis.service.RedisService;
import com.kst.movie_ticket_reservation.security.jwt.JwtService;
import com.kst.movie_ticket_reservation.util.enums.Role;
import com.kst.movie_ticket_reservation.util.events.MailSendEvent;
import com.kst.movie_ticket_reservation.util.exceptions.*;
import com.kst.movie_ticket_reservation.util.job_payloads.MailSendPayload;
import com.kst.movie_ticket_reservation.util.services.UtilService;
import com.kst.movie_ticket_reservation.util.services.html.HtmlService;
import com.kst.movie_ticket_reservation.util.services.otp.OtpService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private final GoogleOAuthService googleOAuthService;
    private final UserRepository userRepository;
    protected final JwtService jwtService;
    private final OtpService otpService;
    private final HtmlService htmlService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UtilService utilService;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    private static final String REQUEST_OTP_AMOUNT_KEY = "req_otp_amount:";
    private static final String VERIFIED_OTP_TOKEN_KEY = "verify_otp_token:";
    private static final String OTP_KEY = "otp:";
    private static final String REQUEST_OTP_TOKEN_KEY = "req_otp_token:";
    private static final String TEMP_BLOCK_USER_KEY = "temp_block_user:";

    @Override
    public RequestOtpResDto requestOtp(RequestOtpDto requestOtpDto) throws TooManyRequestException
    {
        this.validateTempBlockUser(requestOtpDto.getEmail());

        String requestAmountKey = REQUEST_OTP_AMOUNT_KEY + requestOtpDto.getEmail();
        String otpKey = OTP_KEY + requestOtpDto.getEmail();

        Long requestAmount = this.redisService.increaseKey(requestAmountKey);

        log.info("request time is" + requestAmount);

        if (requestAmount > 5)
        {
            this.redisService.delete(requestAmountKey);

            this.redisService.setString(this.generateTempBlockUserKey(requestOtpDto.getEmail()), "TEMP_BLOCK",
                    Duration.ofHours(12));

            throw new TooManyRequestException("you requested too many times. try again next 12 hours");
        }

        String otp = this.otpService.generateOtp();

        String token = this.utilService.generateSecureToken(16);

        log.info("token from secure util service " + token);

        String requestOtpTokenKey = REQUEST_OTP_TOKEN_KEY + requestOtpDto.getEmail();

        this.redisService.setString(requestOtpTokenKey, token, Duration.ofMinutes(5));

        TemporaryStoreOtpPayload temporaryStoreOtpPayload = new TemporaryStoreOtpPayload(otp, false, 0);

        this.redisService.set(otpKey, temporaryStoreOtpPayload, Duration.ofMinutes(5));

        String htmlTemplate = this.htmlService.generateRequestOtpMailSendTemplate(otp);

        MailSendPayload mailSendPayload = new MailSendPayload(requestOtpDto.getEmail(), "OTP for account register",
                htmlTemplate);

        this.applicationEventPublisher.publishEvent(new MailSendEvent(mailSendPayload));

        return new RequestOtpResDto(token);
    }

    @Override
    public VerifyOtpResDto verifyOtp(VerifyOtpDto verifyOtpDto) throws BadRequestException, UnauthorizedException,
            TooManyRequestException, RequestTimeoutException
    {
        if (verifyOtpDto.getToken().isEmpty())
        {
            throw new BadRequestException("you need to request otp first");
        }

        this.validateTempBlockUser(verifyOtpDto.getEmail());

        String reqOtpTokenKey = REQUEST_OTP_TOKEN_KEY + verifyOtpDto.getEmail();

        String tokenInRedis = this.redisService.getString(reqOtpTokenKey);

        if (tokenInRedis == null || tokenInRedis.isEmpty())
        {
            throw new RequestTimeoutException("token no longer exist");
        }

        if (!tokenInRedis.equals(verifyOtpDto.getToken()))
        {
            throw new BadRequestException("token must be valid");
        }

        String otpKey = OTP_KEY + verifyOtpDto.getEmail();

        Optional<TemporaryStoreOtpPayload> otpPayload = this.redisService.get(otpKey,
                TemporaryStoreOtpPayload.class);

        if (otpPayload.isEmpty())
        {
            throw new RequestTimeoutException("otp expired or exist");
        }

        if (otpPayload.get().getRetryAttempt() >= 5)
        {
            this.redisService.delete(otpKey);

            throw new TooManyRequestException("too many incorrect attempt");
        }

        if (!otpPayload.get().getOtp().equals(verifyOtpDto.getOtp()))
        {
            otpPayload.get().setRetryAttempt(otpPayload.get().getRetryAttempt() + 1);
            this.redisService.set(otpKey, otpPayload.get(), Duration.ofMinutes(5));

            throw new BadRequestException("wrong otp");
        }

        this.redisService.delete(otpKey);

        String token = this.utilService.generateSecureToken(16);

        String verifiedOtpTokenKey = VERIFIED_OTP_TOKEN_KEY + verifyOtpDto.getEmail();

        this.redisService.setString(verifiedOtpTokenKey, token, Duration.ofHours(12));

        return new VerifyOtpResDto(token);

    }

    @Override
    public CommonAuthResDto<UserResDto> register(RegisterDto registerDto) throws UnauthorizedException,
            BadRequestException, TooManyRequestException, RequestTimeoutException
    {
        this.validateTempBlockUser(registerDto.getEmail());

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword()))
        {
            throw new BadRequestException("password and confirm must be same");
        }

        String verifiedOtpTokenKey = VERIFIED_OTP_TOKEN_KEY + registerDto.getEmail();

        Optional<User> existingUserByEmail = this.userRepository.findByEmail(registerDto.getEmail());

        if (existingUserByEmail.isPresent())
        {
            throw new UnauthorizedException("email already exist");
        }

        Optional<User> existingUserByGoogleEmail = this.userRepository.findByGoogleEmail(registerDto.getEmail());

        if (existingUserByGoogleEmail.isPresent())
        {
            throw new UnauthorizedException("user with that google mail already exist");
        }

        String tokenInRedis = this.redisService.getString(verifiedOtpTokenKey);

        if (tokenInRedis == null || tokenInRedis.isEmpty())
        {
            throw new RequestTimeoutException("otp verified expired");
        }

        if (!tokenInRedis.equals(registerDto.getToken()))
        {
            throw new BadRequestException("wrong token");
        }

        String hash = this.passwordEncoder.encode(registerDto.getPassword());

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        User newUser = new User();
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(hash);
        newUser.setRoles(roles);

        User registeredUser = this.userRepository.save(newUser);

        String accessToken = this.jwtService.generateAccessToken(registeredUser.getId(), registeredUser.getEmail(),
                registeredUser.getRoles());

        String refreshToken = this.jwtService.generateRefreshToken(registeredUser.getId(), registeredUser.getEmail(),
                registeredUser.getRoles());

        String requestAmountKey = REQUEST_OTP_AMOUNT_KEY + registerDto.getEmail();
        this.redisService.delete(requestAmountKey);
        this.redisService.delete(verifiedOtpTokenKey);

        return new CommonAuthResDto<>(accessToken, refreshToken, new UserResDto(registeredUser.getId(),
                registeredUser.getName(),
                registeredUser.getEmail(), registeredUser.getGoogleEmail(), registeredUser.getPhone(),
                registeredUser.getRoles()));
    }

    @Override
    public CommonAuthResDto<UserResDto> signIn(SignInDto signInDto) throws UnauthorizedException
    {
        Optional<User> existingUserByEmail = this.userRepository.findByEmail(signInDto.getEmail());

        if (existingUserByEmail.isEmpty())
        {
            throw new UnauthorizedException("invalid");
        }

        Optional<User> existingUserByGoogleEmail = this.userRepository.findByGoogleEmail(signInDto.getEmail());

        if (existingUserByGoogleEmail.isPresent())
        {
            throw new UnauthorizedException("invalid");
        }

        if (!this.passwordEncoder.matches(signInDto.getPassword(), existingUserByEmail.get().getPassword()))
        {
            throw new UnauthorizedException("invalid");
        }

        String accessToken = this.jwtService.generateAccessToken(existingUserByEmail.get().getId(),
                existingUserByEmail.get().getEmail(), existingUserByEmail.get().getRoles());

        String refreshToken = this.jwtService.generateRefreshToken(existingUserByEmail.get().getId(),
                existingUserByEmail.get().getEmail(), existingUserByEmail.get().getRoles());

        return new CommonAuthResDto<>(accessToken, refreshToken, new UserResDto(existingUserByEmail.get().getId(),
                existingUserByEmail.get().getName(), existingUserByEmail.get().getEmail(),
                existingUserByEmail.get().getGoogleEmail(), existingUserByEmail.get().getPhone(),
                existingUserByEmail.get().getRoles()));

    }


    @Override
    public CommonAuthResDto<UserResDto> googleSignIn(String idToken) throws Exception
    {
        Map<String, Object> userDetails = this.googleOAuthService.verifyIdToken(idToken);

        this.userRepository.findByEmail(userDetails.get("googleEmail").toString()).orElseThrow(() ->
                new UnauthorizedException("user with this email already exist"));

        this.userRepository.findByGoogleEmail(userDetails.get("googleEmail").toString()).orElseThrow(() ->
                new UnauthorizedException("User with that google email already exist"));

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        User newUser = new User();
        newUser.setName(userDetails.get("name").toString());
        newUser.setGoogleId(userDetails.get("googleId").toString());
        newUser.setGoogleEmail(userDetails.get("googleEmail").toString());
        newUser.setRoles(roles);

        User googleSignedInUser = this.userRepository.save(newUser);

        String accessToken = this.jwtService.generateAccessToken(googleSignedInUser.getId(),
                googleSignedInUser.getGoogleEmail(), googleSignedInUser.getRoles());

        String refreshToken = this.jwtService.generateRefreshToken(googleSignedInUser.getId()
                , googleSignedInUser.getGoogleEmail(), googleSignedInUser.getRoles());

        return new CommonAuthResDto<>(accessToken, refreshToken,
                new UserResDto(googleSignedInUser.getId(), googleSignedInUser.getName(),
                        googleSignedInUser.getEmail(),
                        googleSignedInUser.getGoogleEmail(), googleSignedInUser.getPhone(),
                        googleSignedInUser.getRoles()));
    }

    @Override
    public CompleteRegisterResDto completeRegister(Long id, CompleteRegisterDto completeRegisterDto) throws NotFoundException
    {
        User existingUser = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));

        existingUser.setName(completeRegisterDto.getName());
        existingUser.setPhone(completeRegisterDto.getPhone());

        User updatedProfile = this.userRepository.save(existingUser);

        return new CompleteRegisterResDto(updatedProfile.getId(), updatedProfile.getName(), updatedProfile.getEmail(),
                updatedProfile.getGoogleEmail(), updatedProfile.getPhone(), updatedProfile.getRoles());
    }

    @Override
    public CommonAuthResDto<UserResDto> refresh(TokenRefreshDto tokenRefreshDto) throws UnauthorizedException
    {
        String email = this.jwtService.extractEmail(tokenRefreshDto.getRefreshToken());

        if (email == null || email.isEmpty())
        {
            throw new UnauthorizedException("invalid");
        }

        User activeUser;
        String activeUserEmail;

        Optional<User> existingUserByEmail = this.userRepository.findByEmail(email);

        if (existingUserByEmail.isPresent())
        {
            activeUser = existingUserByEmail.get();
            activeUserEmail = existingUserByEmail.get().getEmail();
        }
        else
        {
            User existingUserByGoogleEmail =
                    this.userRepository.findByGoogleEmail(email).orElseThrow(() -> new UnauthorizedException("invalid"
                    ));

            activeUser = existingUserByGoogleEmail;
            activeUserEmail = existingUserByGoogleEmail.getGoogleEmail();
        }

        String accessToken = this.jwtService.generateAccessToken(activeUser.getId(), activeUserEmail,
                activeUser.getRoles());

        String refreshToken = this.jwtService.generateAccessToken(activeUser.getId(), activeUserEmail,
                activeUser.getRoles());

        return new CommonAuthResDto<>(accessToken, refreshToken, new UserResDto(activeUser.getId(),
                activeUser.getName(), activeUser.getEmail(), activeUser.getGoogleEmail(), activeUser.getPhone(),
                activeUser.getRoles()));
    }

    @Override
    public void verifyRequestOtpToken(VerifyRequestOtpTokenDto verifyRequestOtpTokenDto) throws UnauthorizedException
    {
        String requestOtpTokenKey = "request_otp_token_key:" + verifyRequestOtpTokenDto.getEmail();

        String token = this.redisService.getString(requestOtpTokenKey);

        if (token == null || token.isEmpty())
        {
            throw new UnauthorizedException("invalid token");
        }

    }

    private void validateTempBlockUser(String email) throws TooManyRequestException
    {
        String tempBlockUserKey = this.generateTempBlockUserKey(email);

        String tempBlockUserPayloadStr = this.redisService.getString(tempBlockUserKey);

        if (tempBlockUserPayloadStr != null && tempBlockUserPayloadStr.equals("TEMP_BLOCK"))
        {
            throw new TooManyRequestException("you are under temporary block for abusing auth api");
        }
    }

    private String generateTempBlockUserKey(String email)
    {
        return TEMP_BLOCK_USER_KEY + email;
    }
}
