package com.kst.movie_ticket_reservation.feat.show_date.dto.req;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateShowDateDto
{
    @NotNull(message = "show display date is required")
    @FutureOrPresent(message = "show display date must be present or future")
    private Instant showDisplayDate;

    @NotNull(message = "theatre is required")
    private Long theatreId;

    @NotNull(message = "movie id is required")
    private Long movieId;

}
