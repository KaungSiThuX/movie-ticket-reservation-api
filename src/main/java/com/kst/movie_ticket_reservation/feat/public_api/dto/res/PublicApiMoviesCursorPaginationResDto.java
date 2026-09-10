package com.kst.movie_ticket_reservation.feat.public_api.dto.res;

import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.util.api_responses.CursorPaginationMetaData;

import java.util.List;

public record PublicApiMoviesCursorPaginationResDto(List<PublicApiMovieResDto> movieList,
                                                    CursorPaginationMetaData cursorPaginationMetaData)
{
}
