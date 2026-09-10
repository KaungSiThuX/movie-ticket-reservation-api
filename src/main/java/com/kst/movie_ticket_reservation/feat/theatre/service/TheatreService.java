package com.kst.movie_ticket_reservation.feat.theatre.service;

import com.kst.movie_ticket_reservation.feat.theatre.dto.req.CreateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.req.UpdateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreNameResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreResDto;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface TheatreService
{
    TheatreResDto create(CreateTheatreDto createTheatreDto) throws ConflictException;

    TheatreResDto update(Long id, UpdateTheatreDto updateTheatreDto) throws NotFoundException, ConflictException;

    TheatreResDto delete(Long id) throws NotFoundException;

    TheatreResDto hide(Long id) throws NotFoundException;

    TheatreResDto show(Long id) throws NotFoundException;

    TheatreOffsetPaginationResDto findMany(int offset, int limit);

    TheatreResDto findById(Long id) throws NotFoundException;

    Theatre findExistingTheatreById(Long id) throws NotFoundException;

    List<TheatreNameResDto> findAll();
}