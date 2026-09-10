package com.kst.movie_ticket_reservation.feat.public_api.service;

import com.kst.movie_ticket_reservation.feat.public_api.dto.res.PublicApiMovieDetailsResDto;
import com.kst.movie_ticket_reservation.feat.public_api.dto.res.PublicApiMoviesCursorPaginationResDto;
import com.kst.movie_ticket_reservation.feat.public_api.dto.res.PublicApiShowDateResDto;
import com.kst.movie_ticket_reservation.feat.public_api.dto.res.PublicApiShowSeatResDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface PublicApiService
{
    List<ShowSeatResDto> findManyShowSeat(String showTimePublicId) throws NotFoundException;

    List<PublicApiShowDateResDto> findManyShowDate(Long movieId);

    PublicApiMoviesCursorPaginationResDto findManyMoviesByCursor(Long cursor, int limit);

    PublicApiMovieDetailsResDto findMovieDetails(Long movieId) throws NotFoundException;

    List<PublicApiShowSeatResDto> findAllShowSeatsByShowTimePublicId(String showTimePublicId) throws NotFoundException;
}
