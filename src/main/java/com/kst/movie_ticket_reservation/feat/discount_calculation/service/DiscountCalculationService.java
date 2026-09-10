package com.kst.movie_ticket_reservation.feat.discount_calculation.service;

import com.kst.movie_ticket_reservation.feat.discount_calculation.dto.res.DiscountCalculationResDto;
import com.kst.movie_ticket_reservation.feat.order.entity.Order;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;

import java.util.List;

public interface DiscountCalculationService
{
    public DiscountCalculationResDto calculate(Order order, List<ShowSeat> showSeatList, String promoCode);
}
