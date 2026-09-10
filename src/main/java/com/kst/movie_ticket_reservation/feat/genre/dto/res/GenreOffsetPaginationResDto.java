package com.kst.movie_ticket_reservation.feat.genre.dto.res;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record GenreOffsetPaginationResDto(List<GenreResDto> genreResDtoList,
                                          OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{
}
