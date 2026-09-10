package com.kst.movie_ticket_reservation.feat.public_api.dto.res;

import com.kst.movie_ticket_reservation.util.enums.SeatType;
import com.kst.movie_ticket_reservation.util.enums.ShowSeatStatus;

import java.math.BigDecimal;

public record PublicApiShowSeatResDto(String id, String row, String seatNumber, SeatType seatType, BigDecimal basePrice,
                                      ShowSeatStatus showSeatStatus)
{
}
