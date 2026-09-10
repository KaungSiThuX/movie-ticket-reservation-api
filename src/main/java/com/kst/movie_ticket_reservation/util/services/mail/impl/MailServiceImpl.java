package com.kst.movie_ticket_reservation.util.services.mail.impl;

import com.kst.movie_ticket_reservation.integration.resend.service.ResendService;
import com.kst.movie_ticket_reservation.util.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService
{
    private final ResendService resendService;

    @Override
    public void send(String toMail, String subject, String htmlTemplate) throws Exception
    {
        this.resendService.send(toMail, subject, htmlTemplate);
    }
}
