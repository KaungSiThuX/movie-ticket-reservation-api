package com.kst.movie_ticket_reservation.util.job_payloads;

import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;

import java.util.List;

public record SeatEventPayload(Long currentPersonId, Long showTimeId, List<ShowSeat> showSeatList)
{
}
