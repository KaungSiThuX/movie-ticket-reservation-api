package com.kst.movie_ticket_reservation.feat.promo_code.dto.res;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;

import java.util.List;

public record PromoCodeOffsetPaginationResDto(List<PromoCodeResDto> promoCodeResDtoList,
                                              OffsetPaginationApiMetaData offsetPaginationApiMetaData)
{
}
