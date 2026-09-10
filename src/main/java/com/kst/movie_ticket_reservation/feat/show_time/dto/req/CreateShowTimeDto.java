package com.kst.movie_ticket_reservation.feat.show_time.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateShowTimeDto
{
    @NotNull(message = "show date id required")
    private Long showDateId;

    @NotNull(message = "show display times is required")
    private Set<String> showDisplayTimes;
}
