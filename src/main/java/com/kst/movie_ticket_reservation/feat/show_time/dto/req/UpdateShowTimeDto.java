package com.kst.movie_ticket_reservation.feat.show_time.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateShowTimeDto
{
//    @NotNull(message = "show date id required")
//    private Long showDateId;

    @NotNull(message = "show display time is required")
    private String showDisplayTime;
}
