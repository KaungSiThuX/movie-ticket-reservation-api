package com.kst.movie_ticket_reservation.util.events;

import com.kst.movie_ticket_reservation.util.job_payloads.SeatUnlockPayload;

public record SeatUnlockEvent(SeatUnlockPayload seatUnlockPayload)
{
}
