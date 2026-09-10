package com.kst.movie_ticket_reservation.feat.seat.dto.res;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record SeatOffsetPaginationResDto(List<SeatResDto> seatResDtoList,
                                         OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{
}
