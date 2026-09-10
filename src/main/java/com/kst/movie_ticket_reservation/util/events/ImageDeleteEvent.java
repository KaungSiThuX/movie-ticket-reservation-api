package com.kst.movie_ticket_reservation.util.events;

import com.kst.movie_ticket_reservation.util.job_payloads.ImageDeletePayload;

public record ImageDeleteEvent(ImageDeletePayload imageDeletePayload)
{
}
