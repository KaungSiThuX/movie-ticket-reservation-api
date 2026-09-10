package com.kst.movie_ticket_reservation.feat.show_date.dto.res;


import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record ShowDateOffsetPaginationResDto(List<ShowDateResDto> showDateResDtoList,
                                             OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{

}
