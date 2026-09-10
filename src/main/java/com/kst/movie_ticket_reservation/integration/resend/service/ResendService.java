package com.kst.movie_ticket_reservation.integration.resend.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResendService
{
    private final Resend resend;

    public void send(String toMail, String subject, String htmlTemplate) throws Exception
    {
        CreateEmailOptions createEmailOptions = new CreateEmailOptions.Builder()
                .from("Kaung Cinema <ticket@reservation.kaungsithu.online>")
                .to(toMail)
                .subject(subject)
                .html(htmlTemplate)
                .build();

        try
        {
            CreateEmailResponse data = resend.emails().send(createEmailOptions);
            System.out.println(data.getId());
        }
        catch (ResendException e)
        {
            throw new Exception(e.getLocalizedMessage());
        }
    }
}
