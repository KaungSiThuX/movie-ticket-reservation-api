package com.kst.movie_ticket_reservation.feat.cast.dto.req;

import com.kst.movie_ticket_reservation.util.enums.CastType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateCastDto
{

    @NotBlank(message = "name is required")
    @Length(min = 1, max = 50, message = "name must be within 1 and 50 characters")
    private String name;

    @NotBlank(message = "name is required")
    @Length(min = 1, max = 60, message = "slug must be within 1 and 60 characters")
    @Pattern(
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*+$",
            message = "slug must contain only lowercase letters, numbers, and hyphens"
    )
    private String slug;

    @NotNull(message = "castType is required")
    private CastType castType;
}
