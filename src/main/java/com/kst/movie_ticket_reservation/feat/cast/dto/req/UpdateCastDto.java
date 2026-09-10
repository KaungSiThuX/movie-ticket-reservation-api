package com.kst.movie_ticket_reservation.feat.cast.dto.req;

import com.kst.movie_ticket_reservation.util.enums.CastType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCastDto
{
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "name is required")
    private String slug;

    @NotNull(message = "cast type is required")
    private CastType castType;
}
