package com.kst.movie_ticket_reservation.feat.theatre.dto.res;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record TheatreOffsetPaginationResDto(List<TheatreResDto> theatreResDtoList,
                                            OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{
}
