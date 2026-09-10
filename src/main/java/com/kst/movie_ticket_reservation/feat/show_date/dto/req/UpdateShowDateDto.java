package com.kst.movie_ticket_reservation.feat.show_date.dto.req;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UpdateShowDateDto
{
    @NotNull(message = "show display date is required")
    @FutureOrPresent(message = "show display date must be present or future")
    private Instant showDisplayDate;

    @NotNull(message = "theatre is required")
    private Long theatreId;

    @NotNull(message = "movie id is required")
    private Long movieId;

}