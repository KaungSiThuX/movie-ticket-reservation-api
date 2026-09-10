package com.kst.movie_ticket_reservation.feat.seat.service;

import com.kst.movie_ticket_reservation.feat.seat.dto.req.CreateSeatDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.req.UpdateSeatDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.res.SeatOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.res.SeatResDto;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface SeatService
{
    SeatResDto create(Long theatreId, CreateSeatDto createSeatDto) throws ConflictException, NotFoundException;

    SeatResDto update(Long id, UpdateSeatDto updateSeatDto) throws NotFoundException;

    SeatResDto hide(Long id) throws NotFoundException;

    SeatResDto show(Long id) throws NotFoundException;

    SeatResDto delete(Long id) throws NotFoundException;

    SeatOffsetPaginationResDto findMany(int offset, int limit);

    SeatOffsetPaginationResDto findManyByTheatreId(Long theatreId, int offset, int limit);
}
