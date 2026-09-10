package com.kst.movie_ticket_reservation.feat.cast.service;

import com.kst.movie_ticket_reservation.feat.cast.dto.req.CreateCastDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.req.UpdateCastDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.res.CastOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.res.CastResDto;
import com.kst.movie_ticket_reservation.feat.director.dto.req.CreateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.req.UpdateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorResDto;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface CastService
{
    CastResDto create(CreateCastDto createCastDto) throws ConflictException;

    CastResDto update(Long id, UpdateCastDto updateCastDto) throws NotFoundException, ConflictException;

    CastResDto delete(Long id) throws NotFoundException;

    CastOffsetPaginationResDto findMany(int offset, int limit);

    List<CastResDto> findAll();
}
