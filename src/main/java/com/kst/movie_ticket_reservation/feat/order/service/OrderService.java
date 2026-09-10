package com.kst.movie_ticket_reservation.feat.order.service;


import com.kst.movie_ticket_reservation.feat.order.dto.req.CreateOrderDto;
import com.kst.movie_ticket_reservation.feat.order.dto.res.CreateOrderResDto;
import com.kst.movie_ticket_reservation.feat.order.dto.res.OrderDetailResDto;
import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import com.kst.movie_ticket_reservation.util.exceptions.BadRequestException;
import com.kst.movie_ticket_reservation.util.exceptions.CustomStripeException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.exceptions.UnauthorizedException;
import com.stripe.exception.StripeException;

import java.time.Instant;

public interface OrderService
{
    CreateOrderResDto create(CurrentPerson currentPerson, CreateOrderDto createOrderDto) throws NotFoundException,
            BadRequestException, CustomStripeException, UnauthorizedException;

    void handleWebhook(String payload, String signatureHeader) throws StripeException, BadRequestException,
            NotFoundException, UnauthorizedException, CustomStripeException;

    OrderDetailResDto getOrderDetail(String orderPublicId) throws NotFoundException;

    // byte[] exportPdf(Instant from, Instant to);
}
