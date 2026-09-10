package com.kst.movie_ticket_reservation.feat.seat.dto.res;

import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreResDto;
import com.kst.movie_ticket_reservation.util.enums.SeatType;

import java.util.UUID;

public record SeatResDto(Long id, String row, String seatNumber, SeatType seatType, TheatreResDto theatre)
{
}
