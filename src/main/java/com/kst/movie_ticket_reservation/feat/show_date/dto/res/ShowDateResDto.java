package com.kst.movie_ticket_reservation.feat.show_date.dto.res;

import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieTitleResDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreNameResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreResDto;

import java.time.Instant;
import java.util.List;

public record ShowDateResDto(Long id, Instant showDisplayDate, MovieTitleResDto movie, TheatreNameResDto theatre)
{
}
