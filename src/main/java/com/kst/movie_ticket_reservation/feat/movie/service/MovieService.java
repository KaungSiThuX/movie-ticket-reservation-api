package com.kst.movie_ticket_reservation.feat.movie.service;

import com.kst.movie_ticket_reservation.feat.movie.dto.req.CreateMovieDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.UpdateMovieFormDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.UpdateMovieJsonDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieTitleResDto;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.CustomS3Exception;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieService
{
    MovieResDto createJson(CreateMovieDto createMovieDto) throws ConflictException,
            NotFoundException;

    MovieResDto createForm(CreateMovieDto createMovieDto, MultipartFile poster) throws ConflictException,
            NotFoundException, CustomS3Exception;

    MovieResDto updateJson(Long id, UpdateMovieJsonDto updateMovieJsonDto) throws NotFoundException;

    MovieResDto updateForm(Long id, UpdateMovieFormDto updateMovieFormDto, MultipartFile poster) throws NotFoundException, CustomS3Exception;

    MovieResDto hide(Long id) throws NotFoundException;

    MovieResDto show(Long id) throws NotFoundException;

    MovieResDto delete(Long id) throws NotFoundException;

    MovieOffsetPaginationResDto findMany(int offset, int limit);

    MovieResDto findById(Long id) throws NotFoundException;

    Movie findExistingMovieById(Long id) throws NotFoundException;

    List<MovieTitleResDto> findAll();
}
