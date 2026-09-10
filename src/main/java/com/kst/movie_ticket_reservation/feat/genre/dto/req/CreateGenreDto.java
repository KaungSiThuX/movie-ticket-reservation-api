package com.kst.movie_ticket_reservation.feat.genre.dto.req;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
public class CreateGenreDto
{
    @NotBlank(message = "name is required")
    @Length(min = 1, max = 50, message = "genre name must between 1 and 50 characters")
    private String name;

    @NotBlank(message = "slug is required")
    @Length(min = 1, max = 50, message = "genre slug must between 1 and 50 characters")
    private String slug;
}
