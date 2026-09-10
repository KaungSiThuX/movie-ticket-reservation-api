package com.kst.movie_ticket_reservation.util.pub_sub.subscriber;

import com.kst.movie_ticket_reservation.util.enums.SeatEventType;

public record SeatEventMessage(SeatEventType seatEventType, Object payload)
{
}
