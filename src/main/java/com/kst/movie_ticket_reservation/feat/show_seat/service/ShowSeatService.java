package com.kst.movie_ticket_reservation.feat.show_seat.service;

import com.kst.movie_ticket_reservation.feat.show_seat.dto.req.CreateShowSeatDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.req.UpdateShowSeatDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface ShowSeatService
{
    List<ShowSeatResDto> create(CreateShowSeatDto createShowSeatDto) throws NotFoundException;

    //ShowSeatResDto update(UpdateShowSeatDto updateShowSeatDto) throws NotFoundException;

    ShowSeatOffsetPaginationResDto findManyByOffset(int offset, int limit);

    ShowSeatOffsetPaginationResDto findManyByShowTimeIdOffset(Long showTimeId, int offset, int limit);

}
