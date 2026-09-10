package com.kst.movie_ticket_reservation.feat.public_api.dto.res;

import com.kst.movie_ticket_reservation.util.enums.SeatType;

public record PublicApiSeatResDto(Long id, String row, String seatNumber, SeatType seatType)
{
}
