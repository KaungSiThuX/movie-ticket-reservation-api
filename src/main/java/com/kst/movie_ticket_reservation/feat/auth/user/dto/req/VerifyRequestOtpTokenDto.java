package com.kst.movie_ticket_reservation.feat.auth.user.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRequestOtpTokenDto
{
    private String email;

    private String token;
}
