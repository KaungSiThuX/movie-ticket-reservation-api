package com.kst.movie_ticket_reservation.util.events;

import com.kst.movie_ticket_reservation.util.job_payloads.SeatSoldPayload;

public record SeatSoldEvent(SeatSoldPayload seatSoldPayload)
{
}
