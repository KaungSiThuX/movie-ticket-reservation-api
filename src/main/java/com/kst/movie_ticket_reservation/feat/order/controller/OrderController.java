package com.kst.movie_ticket_reservation.feat.order.controller;


import com.kst.movie_ticket_reservation.feat.order.dto.req.CreateOrderDto;
import com.kst.movie_ticket_reservation.feat.order.dto.res.CreateOrderResDto;
import com.kst.movie_ticket_reservation.feat.order.dto.res.OrderDetailResDto;
import com.kst.movie_ticket_reservation.feat.order.service.OrderService;
import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.BadRequestException;
import com.kst.movie_ticket_reservation.util.exceptions.CustomStripeException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.exceptions.UnauthorizedException;
import com.kst.movie_ticket_reservation.util.handlers.SuccessApiResponseHandler;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController
{
    private final OrderService orderService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{orderId}")
    ResponseEntity<SuccessApiResponse<OrderDetailResDto>> getOrderDetails(@PathVariable String orderId) throws NotFoundException
    {
        OrderDetailResDto orderDetailResDto = this.orderService.getOrderDetail(orderId);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "GET_ORDER_DETAIL_SUCCESS", orderDetailResDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<CreateOrderResDto>> create(@AuthenticationPrincipal CurrentPerson currentPerson,
                                                                 @Valid @RequestBody CreateOrderDto createOrderDto) throws NotFoundException, BadRequestException, CustomStripeException, UnauthorizedException


    {
        CreateOrderResDto createOrderResDto = this.orderService.create(currentPerson, createOrderDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "ORDER_CREATE_SUCCESS", createOrderResDto);
    }


    @PostMapping("webhook")
    void handleWebhook(@RequestBody String payload, @RequestHeader("Stripe" +
            "-Signature") String signatureHeader) throws BadRequestException, NotFoundException, StripeException,
            UnauthorizedException, CustomStripeException
    {
        this.orderService.handleWebhook(payload, signatureHeader);
    }
}

