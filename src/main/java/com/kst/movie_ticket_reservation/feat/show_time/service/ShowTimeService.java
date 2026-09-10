package com.kst.movie_ticket_reservation.feat.show_time.service;

import com.kst.movie_ticket_reservation.feat.show_time.dto.req.CreateShowTimeDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.req.UpdateShowTimeDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeResDto;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface ShowTimeService
{
    List<ShowTimeResDto> create(CreateShowTimeDto createShowTimeDto) throws NotFoundException;

    ShowTimeResDto update(Long id, UpdateShowTimeDto updateShowTimeDto) throws NotFoundException;

    ShowTimeResDto delete(Long id) throws NotFoundException;

    ShowTimeOffsetPaginationResDto findManyByOffset(Long showDateId, int offset, int limit);
}
