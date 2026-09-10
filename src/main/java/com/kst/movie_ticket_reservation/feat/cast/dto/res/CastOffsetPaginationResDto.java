package com.kst.movie_ticket_reservation.feat.cast.dto.res;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record CastOffsetPaginationResDto(List<CastResDto> castResDtoList,
                                         OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{
}
