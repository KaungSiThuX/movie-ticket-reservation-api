package com.kst.movie_ticket_reservation.feat.movie.dto.res;

import com.kst.movie_ticket_reservation.feat.cast.dto.res.CastResDto;
import com.kst.movie_ticket_reservation.feat.cast.entity.Cast;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorResDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreResDto;
import com.kst.movie_ticket_reservation.util.enums.MPARatingType;

import java.util.Set;

public record MovieResDto(Long id, String title, String slug, String description,
                          String poster, String trailer, String releaseYear,
                          MPARatingType mpaRatingType, Integer runTimeMinutes,
                          Set<GenreResDto> genres, Set<DirectorResDto> directors, Set<CastResDto> casts)
{
}
