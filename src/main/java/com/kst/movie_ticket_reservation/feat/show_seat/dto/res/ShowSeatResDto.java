package com.kst.movie_ticket_reservation.feat.show_seat.dto.res;

import com.kst.movie_ticket_reservation.util.enums.SeatType;
import com.kst.movie_ticket_reservation.util.enums.ShowSeatStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ShowSeatResDto(Long id, String row, String seatNumber, SeatType seatType,
                             BigDecimal basePrice,
                             ShowSeatStatus showSeatStatus
)
{
}
