package com.kst.movie_ticket_reservation.feat.show_seat.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateShowSeatDto
{
//    @NotNull(message = "theatre id is required")
//    private Long theatreId;

    @NotNull(message = "show time id is required")
    private Long showTimeId;

    @NotNull(message = "show seat prices is required")
    private Set<ShowSeatPrice> showSeatPrices;
}
