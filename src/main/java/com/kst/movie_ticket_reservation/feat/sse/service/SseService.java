package com.kst.movie_ticket_reservation.feat.sse.service;

import com.kst.movie_ticket_reservation.util.enums.SeatEventType;
import com.kst.movie_ticket_reservation.util.pub_sub.subscriber.SeatEventMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService
{
    void broadcast(String eventName, Object payload);

    void publish(SeatEventType seatEventType, Object payload);

    SseEmitter subscribe();
}
