package com.kst.movie_ticket_reservation.feat.order.dto.res;

import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDetailResDto(String orderId, String movieTitle, List<String> movieGenres, Instant showDisplayDate,
                                String showDisplayTime, String theatreName,
                                List<ShowSeatResDto> orderedShowSeats, BigDecimal orderTotal,
                                BigDecimal subTotal, BigDecimal promoCodeDiscount, BigDecimal tax)
{

}
