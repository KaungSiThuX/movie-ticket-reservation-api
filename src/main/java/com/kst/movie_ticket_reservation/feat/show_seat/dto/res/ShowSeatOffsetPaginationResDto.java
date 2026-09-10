package com.kst.movie_ticket_reservation.feat.show_seat.dto.res;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record ShowSeatOffsetPaginationResDto(List<ShowSeatResDto> seatResDtoList,
                                             OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{
}
