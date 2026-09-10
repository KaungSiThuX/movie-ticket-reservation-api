//package com.kst.movie_ticket_reservation.feat.discount_calculation.service.impl;
//
//import com.kst.movie_ticket_reservation.feat.discount_calculation.dto.res.DiscountCalculationResDto;
//import com.kst.movie_ticket_reservation.feat.discount_calculation.service.DiscountCalculationService;
//import com.kst.movie_ticket_reservation.feat.order.entity.Order;
//import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class DiscountCalculationServiceImpl implements DiscountCalculationService
//{
//    @Override
//    public DiscountCalculationResDto calculate(Order order, List<ShowSeat> showSeatList, String promoCode)
//    {
//        BigDecimal subTotal = showSeatList.stream().map(ShowSeat::getBasePrice).reduce(BigDecimal.ZERO,
//                BigDecimal::add);
//
//        if (promoCode != null && !promoCode.isEmpty())
//        {
//
//        }
//    }
//}
