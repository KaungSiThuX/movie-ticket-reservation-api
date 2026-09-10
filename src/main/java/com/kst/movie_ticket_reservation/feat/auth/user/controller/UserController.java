package com.kst.movie_ticket_reservation.feat.auth.user.controller;

import com.kst.movie_ticket_reservation.feat.auth.dto.res.CommonAuthResDto;
import com.kst.movie_ticket_reservation.feat.auth.user.dto.req.*;
import com.kst.movie_ticket_reservation.feat.auth.user.dto.res.*;
import com.kst.movie_ticket_reservation.feat.auth.user.service.UserService;
import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.*;
import com.kst.movie_ticket_reservation.util.handlers.SuccessApiResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PostMapping("google-sign-in")
    ResponseEntity<SuccessApiResponse<CommonAuthResDto<UserResDto>>> googleSignIn(@Valid @RequestBody GoogleSignInDto googleSignInDto) throws Exception
    {
        CommonAuthResDto<UserResDto> commonAuthResDto =
                this.userService.googleSignIn(googleSignInDto.getIdToken());

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "GOOGLE_SIGN_IN_SUCCESS", commonAuthResDto);
    }

    @PostMapping("request-otp")
    ResponseEntity<SuccessApiResponse<RequestOtpResDto>> requestOtp(@Valid @RequestBody RequestOtpDto requestOtpDto) throws TooManyRequestException
    {
        RequestOtpResDto requestOtpResDto = this.userService.requestOtp(requestOtpDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "REQUEST_OTP_SUCCESS", requestOtpResDto);
    }

    @PostMapping("request-otp-token")
    ResponseEntity<SuccessApiResponse<String>> requestOtpToken(@Valid @RequestBody VerifyRequestOtpTokenDto verifyRequestOtpTokenDto) throws UnauthorizedException
    {
        this.userService.verifyRequestOtpToken(verifyRequestOtpTokenDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "REQUEST_OTP_TOKEN_TRUE", null);
    }

    @PostMapping("verify-otp")
    ResponseEntity<SuccessApiResponse<VerifyOtpResDto>> verifyOtp(@Valid @RequestBody VerifyOtpDto verifyOtpDto) throws TooManyRequestException, UnauthorizedException, BadRequestException, RequestTimeoutException
    {
        VerifyOtpResDto verifyOtpResDto = this.userService.verifyOtp(verifyOtpDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "VERIFY_OTP_SUCCESS", verifyOtpResDto);
    }

    @PostMapping("register")
    ResponseEntity<SuccessApiResponse<CommonAuthResDto<UserResDto>>> register(@Valid @RequestBody RegisterDto registerDto) throws UnauthorizedException, BadRequestException, RequestTimeoutException, TooManyRequestException
    {
        CommonAuthResDto<UserResDto> commonAuthResDto = this.userService.register(registerDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "REGISTER_SUCCESS", commonAuthResDto);
    }

    @PostMapping("sign-in")
    ResponseEntity<SuccessApiResponse<CommonAuthResDto<UserResDto>>> signIn(@Valid @RequestBody SignInDto signInDto) throws UnauthorizedException
    {
        CommonAuthResDto<UserResDto> commonAuthResDto = this.userService.signIn(signInDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "SIGN_IN_SUCCESS", commonAuthResDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("complete-register")
    ResponseEntity<SuccessApiResponse<CompleteRegisterResDto>> completeRegister(@AuthenticationPrincipal CurrentPerson currentPerson, @Valid @RequestBody CompleteRegisterDto completeRegisterDto) throws NotFoundException
    {
        CompleteRegisterResDto completeRegisterResDto = this.userService.completeRegister(currentPerson.getId(),
                completeRegisterDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "COMPLETE_REGISTER_SUCCESS", completeRegisterResDto);
    }

    @PostMapping("refresh")
    ResponseEntity<SuccessApiResponse<CommonAuthResDto<UserResDto>>> refresh(@Valid @RequestBody TokenRefreshDto tokenRefreshDto) throws UnauthorizedException
    {
        CommonAuthResDto<UserResDto> commonAuthResDto = this.userService.refresh(tokenRefreshDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "TOKEN_REFRESH_SUCCESS", commonAuthResDto);
    }

//    @PostMapping("sign-in")
//    ResponseEntity<SuccessApiResponse<CommonAuthResDto<SignInResDto>>> signIn(@Valid @RequestBody SignInDto
//    signInDto) throws UnauthorizedException
//    {
//        CommonAuthResDto<SignInResDto> signInResDtoCommonAuthResDto = this.userService.signIn(signInDto);
//
//        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
//                "SIGN_IN_SUCCESS", signInResDtoCommonAuthResDto);
//    }

}
