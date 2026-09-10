package com.kst.movie_ticket_reservation.feat.theatre.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateTheatreDto
{
    @NotBlank(message = "name is required")
    @Length(min = 1, max = 20, message = "theatre name must between 1 and 20 characters")
    private String name;

    @NotBlank(message = "slug is required")
    @Length(min = 1, max = 20, message = "theatre slug must between 1 and 20 characters")
    private String slug;
}