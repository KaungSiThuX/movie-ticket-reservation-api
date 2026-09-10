package com.kst.movie_ticket_reservation.feat.public_api.dto.res;

import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateResDto;

import java.util.List;

public record PublicApiMovieResDto(Long id, String title, String slug, String poster, String runTimeMinutes,
                                   List<PublicApiGenreResDto> genres)
{
}
