package com.kst.movie_ticket_reservation.feat.auth.user.dto.req;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshDto
{
    @NotBlank(message = "refresh token is required")
    private String refreshToken;
}
