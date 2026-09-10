package com.kst.movie_ticket_reservation.feat.public_api.dto.res;

import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateResDto;
import com.kst.movie_ticket_reservation.util.enums.MPARatingType;

import java.util.List;

public record PublicApiMovieDetailsResDto(Long id, String title, String slug, String poster, String trailer,
                                          String releaseYear, String runTimeMinutes, MPARatingType mpaRatingType,
                                          String description, List<PublicApiGenreResDto> genres,

                                          List<PublicApiShowDateResDto> showDates)
{
}
