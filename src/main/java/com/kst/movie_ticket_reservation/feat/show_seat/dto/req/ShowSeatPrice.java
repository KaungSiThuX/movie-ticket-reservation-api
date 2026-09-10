package com.kst.movie_ticket_reservation.feat.show_seat.dto.req;

import com.kst.movie_ticket_reservation.util.enums.SeatType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ShowSeatPrice
{
    private SeatType seatType;

    private BigDecimal price;
}

