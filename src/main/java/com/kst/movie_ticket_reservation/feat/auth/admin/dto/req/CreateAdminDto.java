package com.kst.movie_ticket_reservation.feat.auth.admin.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateAdminDto
{
    @NotBlank(message = "name is required")
    private String name;

    @Email(message = "must be valid email")
    private String email;

    @NotBlank(message = "password is need")
    @Length(min = 8, max = 8, message = "password must be 8 characters")
    private String password;
}
