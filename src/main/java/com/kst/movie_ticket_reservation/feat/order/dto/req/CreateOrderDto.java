package com.kst.movie_ticket_reservation.feat.order.dto.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CreateOrderDto
{
    private String showTimePublicId;

    private List<String> showSeatPublicIds;

    private String promoCode;
}
