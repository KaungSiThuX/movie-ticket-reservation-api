package com.kst.movie_ticket_reservation.feat.show_seat.dto.req;

import com.kst.movie_ticket_reservation.util.enums.SeatType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class UpdateShowSeatDto
{
    @NotNull(message = "show time id is required")
    private Long showTimeId;

    @NotNull(message = "show seat prices is required")
    private Set<ShowSeatPrice> showSeatPrices;
}
