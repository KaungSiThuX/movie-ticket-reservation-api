package com.kst.movie_ticket_reservation.feat.director.service;

import com.kst.movie_ticket_reservation.feat.director.dto.req.CreateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.req.UpdateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorResDto;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface DirectorService
{
    DirectorResDto create(CreateDirectorDto createDirectorDto) throws ConflictException;

    DirectorResDto update(Long id, UpdateDirectorDto updateDirectorDto) throws NotFoundException, ConflictException;

    DirectorResDto delete(Long id) throws NotFoundException;

    DirectorOffsetPaginationResDto findMany(int offset, int limit);

    List<DirectorResDto> findAll();
}
