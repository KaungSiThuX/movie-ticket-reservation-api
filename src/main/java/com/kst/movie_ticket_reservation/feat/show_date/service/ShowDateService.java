package com.kst.movie_ticket_reservation.feat.show_date.service;

import com.kst.movie_ticket_reservation.feat.show_date.dto.req.CreateShowDateDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.req.UpdateShowDateDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateResDto;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationResponse;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

public interface ShowDateService
{
    ShowDateResDto create(CreateShowDateDto createShowDateDto) throws ConflictException, NotFoundException;

    ShowDateResDto update(Long id, UpdateShowDateDto updateShowDateDto) throws NotFoundException, ConflictException;

    ShowDateResDto delete(Long id) throws NotFoundException;

    ShowDateOffsetPaginationResDto findManyByOffset(int offset, int limit);

    ShowDate findExistingShowDateById(Long id) throws NotFoundException;
}
