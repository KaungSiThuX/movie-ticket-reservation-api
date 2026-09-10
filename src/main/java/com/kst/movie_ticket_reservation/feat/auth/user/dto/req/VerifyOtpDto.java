package com.kst.movie_ticket_reservation.feat.auth.user.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpDto
{
    private String email;

    private String otp;

    private String token;
}
