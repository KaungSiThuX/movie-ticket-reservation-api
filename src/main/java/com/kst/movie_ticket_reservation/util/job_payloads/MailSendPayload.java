package com.kst.movie_ticket_reservation.util.job_payloads;

public record MailSendPayload(String toMail, String subject, String htmlTemplate)
{
}
