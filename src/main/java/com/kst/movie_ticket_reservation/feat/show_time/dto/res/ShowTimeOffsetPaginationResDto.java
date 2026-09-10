package com.kst.movie_ticket_reservation.feat.show_time.dto.res;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record ShowTimeOffsetPaginationResDto(List<ShowTimeResDto> showTimeResDtoList,
                                             OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{
}
