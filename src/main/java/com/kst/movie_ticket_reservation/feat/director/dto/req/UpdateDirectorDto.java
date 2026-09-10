package com.kst.movie_ticket_reservation.feat.director.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDirectorDto
{
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "name is required")
    private String slug;
}
