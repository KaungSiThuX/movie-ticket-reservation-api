package com.kst.movie_ticket_reservation.util.events;

import com.kst.movie_ticket_reservation.util.job_payloads.MailSendPayload;

public record MailSendEvent(MailSendPayload mailSendPayload)
{
}
