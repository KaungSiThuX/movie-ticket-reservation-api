package com.kst.movie_ticket_reservation.feat.director.dto.res;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record DirectorOffsetPaginationResDto(List<DirectorResDto> directorResDtoList,
                                             OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{
}
