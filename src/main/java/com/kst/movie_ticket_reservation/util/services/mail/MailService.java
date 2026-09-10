package com.kst.movie_ticket_reservation.util.services.mail;

public interface MailService
{
    void send(String toMail, String subject, String htmlTemplate) throws Exception;
}
