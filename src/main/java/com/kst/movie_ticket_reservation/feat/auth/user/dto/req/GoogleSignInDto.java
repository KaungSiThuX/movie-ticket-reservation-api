package com.kst.movie_ticket_reservation.feat.auth.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleSignInDto
{
    @NotBlank(message = "id token is required")
    private String idToken;
}
