package com.kst.movie_ticket_reservation.feat.promo_code.service;

import com.kst.movie_ticket_reservation.feat.promo_code.dto.req.CreatePromoCodeDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.req.UpdatePromoCodeDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.res.PromoCodeOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.res.PromoCodeResDto;
import com.kst.movie_ticket_reservation.feat.promo_code.entity.PromoCode;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.util.exceptions.BadRequestException;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;

import java.util.List;

public interface PromoCodeService
{
    PromoCodeResDto create(CreatePromoCodeDto createPromoCodeDto) throws ConflictException;

    PromoCodeResDto update(Long id, UpdatePromoCodeDto updatePromoCodeDto) throws NotFoundException;

    PromoCodeResDto delete(Long id) throws NotFoundException;

    PromoCodeOffsetPaginationResDto findMany(int offset, int limit);

    PromoCode validatePromoCode(Long customerId, String code, List<ShowSeat> showSeatList) throws NotFoundException,
            BadRequestException;
}
