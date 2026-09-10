package com.kst.movie_ticket_reservation.feat.genre.service;

import com.kst.movie_ticket_reservation.feat.genre.dto.req.CreateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.req.UpdateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreResDto;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface GenreService
{
    GenreResDto create(CreateGenreDto createGenreDto) throws ConflictException;

    GenreResDto update(Long id, UpdateGenreDto updateGenreDto) throws NotFoundException, ConflictException;

    GenreResDto delete(Long id) throws NotFoundException;

    GenreResDto hide(Long id) throws NotFoundException;

    GenreResDto show(Long id) throws NotFoundException;

    GenreOffsetPaginationResDto findMany(int offset, int limit);

    GenreResDto findById(Long id) throws NotFoundException;

    List<GenreResDto> findAll();
}
