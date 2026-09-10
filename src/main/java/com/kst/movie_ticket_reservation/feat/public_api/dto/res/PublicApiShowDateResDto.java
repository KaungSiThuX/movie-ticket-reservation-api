package com.kst.movie_ticket_reservation.feat.public_api.dto.res;

import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreResDto;

import java.time.Instant;
import java.util.List;

public record PublicApiShowDateResDto(String id, Instant showDisplayDate, List<PublicApiShowTimeResDto> showTimes,
                                      PublicApiTheatreResDto theatre)
{
}
