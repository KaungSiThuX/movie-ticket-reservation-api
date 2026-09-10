package com.kst.movie_ticket_reservation.feat.auth.user.service;

import com.kst.movie_ticket_reservation.feat.auth.admin.dto.res.AdminResDto;
import com.kst.movie_ticket_reservation.feat.auth.dto.res.CommonAuthResDto;
import com.kst.movie_ticket_reservation.feat.auth.user.dto.req.*;
import com.kst.movie_ticket_reservation.feat.auth.user.dto.res.*;
import com.kst.movie_ticket_reservation.util.exceptions.*;

public interface UserService
{
    RequestOtpResDto requestOtp(RequestOtpDto requestOtpDto) throws TooManyRequestException;

    VerifyOtpResDto verifyOtp(VerifyOtpDto verifyOtpDto) throws BadRequestException, UnauthorizedException,
            TooManyRequestException, RequestTimeoutException;

    CommonAuthResDto<UserResDto> register(RegisterDto registerDto) throws UnauthorizedException,
            BadRequestException, TooManyRequestException, RequestTimeoutException;

    CommonAuthResDto<UserResDto> signIn(SignInDto signInDto) throws UnauthorizedException;

    CommonAuthResDto<UserResDto> googleSignIn(String idToken) throws Exception;

    CompleteRegisterResDto completeRegister(Long id, CompleteRegisterDto completeRegisterDto) throws NotFoundException;

    CommonAuthResDto<UserResDto> refresh(TokenRefreshDto tokenRefreshDto) throws UnauthorizedException;

    void verifyRequestOtpToken(VerifyRequestOtpTokenDto verifyRequestOtpTokenDto) throws UnauthorizedException;
}
