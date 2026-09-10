package com.kst.movie_ticket_reservation.feat.movie.dto.req;

import com.kst.movie_ticket_reservation.util.enums.MPARatingType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
public class UpdateMovieJsonDto
{
    @NotBlank(message = "title is required")
    @Length(min = 1, max = 50, message = "title must be within 1 and 50 characters")
    private String title;

    @NotBlank(message = "slug is required")
    @Length(min = 1, max = 60, message = "slug must be within 1 and 60 characters")
    @Pattern(
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*+$",
            message = "slug must contain only lowercase letters, numbers, and hyphens"
    )
    private String slug;

    @NotBlank(message = "description is required")
    @Size(max = 2000, message = "max size 2000 accept")
    private String description;

    @NotNull(message = "genreIds are required")
    private Set<Long> genreIds;

    private Set<Long> directorIds;

    private Set<Long> castIds;

    @NotBlank(message = "releaseYear is required")
    @Min(value = 1850, message = "releaseYear must be 1850 or higher")
    @Max(value = 2200, message = "releaseYear is too far in the future")
    private String releaseYear;

    @NotNull(message = "mpaRatingType is required")
    private MPARatingType mpaRatingType;

    @NotNull(message = "runTimeMinutes is required")
    @Min(value = 1, message = "run time minutes must be at least 1 minutes")
    @Max(value = 2000, message = "run time minutes must be max 2000")
    private Integer runTimeMinutes;

    //  @NotBlank(message = "trailer is required")
    private String trailer;
}
