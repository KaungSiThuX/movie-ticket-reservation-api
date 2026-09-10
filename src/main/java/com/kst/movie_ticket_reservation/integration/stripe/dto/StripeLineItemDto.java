package com.kst.movie_ticket_reservation.integration.stripe.dto;

import com.kst.movie_ticket_reservation.util.enums.SeatType;

import java.math.BigDecimal;
import java.util.UUID;

public record StripeLineItemDto(Long showSeatId, String showSeatPublicId, String row, String seatNumber,
                                SeatType seatType,
                                BigDecimal price)
{
}
