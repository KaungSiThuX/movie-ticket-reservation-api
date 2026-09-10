package com.kst.movie_ticket_reservation.util.queue_job.consumer.impl;

import com.kst.movie_ticket_reservation.integration.rabbit_mq.config.RabbitMQConfig;
import com.kst.movie_ticket_reservation.util.job_payloads.MailSendPayload;
import com.kst.movie_ticket_reservation.util.queue_job.consumer.JobConsumer;
import com.kst.movie_ticket_reservation.util.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
@RequiredArgsConstructor
public class MailSendJobConsumer implements JobConsumer<MailSendPayload>
{
    private final MailService mailService;

    @Override
    @RabbitListener(queues = RabbitMQConfig.mailSendQueueName)
    public void process(MailSendPayload payload) throws Exception
    {
        this.mailService.send(payload.toMail(), payload.subject(), payload.htmlTemplate());
    }
}
