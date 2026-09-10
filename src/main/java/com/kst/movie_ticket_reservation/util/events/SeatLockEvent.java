package com.kst.movie_ticket_reservation.util.events;

import com.kst.movie_ticket_reservation.util.job_payloads.SeatLockPayload;

public record SeatLockEvent(SeatLockPayload seatLockPayload)
{
}
