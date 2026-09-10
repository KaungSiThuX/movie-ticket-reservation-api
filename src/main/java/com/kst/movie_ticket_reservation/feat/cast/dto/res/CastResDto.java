package com.kst.movie_ticket_reservation.feat.cast.dto.res;

import com.kst.movie_ticket_reservation.util.enums.CastType;

public record CastResDto(Long id, String name, String slug, CastType castType)
{
}
