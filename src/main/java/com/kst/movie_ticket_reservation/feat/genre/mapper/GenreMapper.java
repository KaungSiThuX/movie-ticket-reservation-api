package com.kst.movie_ticket_reservation.feat.genre.mapper;

import com.kst.movie_ticket_reservation.feat.genre.dto.req.CreateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.req.UpdateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreResDto;
import com.kst.movie_ticket_reservation.feat.genre.entity.Genre;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMapper
{
    GenreResDto toGenreResDto(Genre genre);

    Genre toGenre(CreateGenreDto createGenreDto);

    void updateGenreFromDto(UpdateGenreDto updateGenreDto, @MappingTarget Genre genre);
}
