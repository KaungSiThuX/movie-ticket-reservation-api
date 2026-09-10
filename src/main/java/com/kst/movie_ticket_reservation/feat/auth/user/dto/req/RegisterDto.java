package com.kst.movie_ticket_reservation.feat.auth.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RegisterDto
{
    private String email;

    @NotBlank(message = "password is required")
    @Length(min = 8, max = 8, message = "password must be 8 chars")
    private String password;

    @NotBlank(message = "confirm password is required")
    @Length(min = 8, max = 8, message = "confirm password must be 8 chars")
    private String confirmPassword;

    private String token;
}
