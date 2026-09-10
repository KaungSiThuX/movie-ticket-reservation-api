package com.kst.movie_ticket_reservation.feat.movie.dto.req;

import com.kst.movie_ticket_reservation.util.enums.MPARatingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateMovieFormDto
{
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "slug is required")
    private String slug;

    @NotBlank(message = "description is required")
    @Size(max = 2000, message = "max size 2000 accept")
    private String description;

    @NotNull(message = "genreIds are required")
    private Set<Long> genreIds;

    @NotNull(message = "directorIds are required")
    private Set<Long> directorIds;

    @NotNull(message = "cast ids are required")
    private Set<Long> castIds;

    @NotBlank(message = "releaseYear is required")
    private String releaseYear;

    @NotNull(message = "mpaRatingType is required")
    private MPARatingType mpaRatingType;

    @NotNull(message = "runTimeMinutes is required")
    private Integer runTimeMinutes;

    @NotBlank(message = "trailer is required")
    private String trailer;

}
