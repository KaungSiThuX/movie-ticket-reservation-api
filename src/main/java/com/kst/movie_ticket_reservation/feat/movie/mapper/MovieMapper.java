package com.kst.movie_ticket_reservation.feat.movie.mapper;

import com.kst.movie_ticket_reservation.feat.director.enitty.Director;
import com.kst.movie_ticket_reservation.feat.director.mapper.DirectorMapper;
import com.kst.movie_ticket_reservation.feat.genre.mapper.GenreMapper;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.CreateMovieDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.UpdateMovieFormDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.UpdateMovieJsonDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieTitleResDto;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {GenreMapper.class, DirectorMapper.class})
public interface MovieMapper
{
    MovieResDto toMovieResDto(Movie movie);

    MovieTitleResDto toMovieTitleResDto(Movie movie);

    Movie toMovie(CreateMovieDto createMovieDto);

    void updateMovieFromDto(UpdateMovieJsonDto updateMovieJsonDto, @MappingTarget Movie movie);

    void updateMovieFromDto(UpdateMovieFormDto updateMovieFormDto, @MappingTarget Movie movie);

}
