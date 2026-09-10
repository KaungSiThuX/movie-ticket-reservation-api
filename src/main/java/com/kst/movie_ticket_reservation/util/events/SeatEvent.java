package com.kst.movie_ticket_reservation.util.events;

import com.kst.movie_ticket_reservation.util.job_payloads.SeatEventPayload;

public record SeatEvent(SeatEventPayload seatEventPayload)
{
}
