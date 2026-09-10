package com.kst.movie_ticket_reservation.util.queue_job.producer.impl;

import com.kst.movie_ticket_reservation.integration.rabbit_mq.config.RabbitMQConfig;
import com.kst.movie_ticket_reservation.util.job_payloads.MailSendPayload;
import com.kst.movie_ticket_reservation.util.queue_job.producer.JobProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSendJobProducer implements JobProducer<MailSendPayload>
{
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void produce(MailSendPayload payload)
    {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName, RabbitMQConfig.mailSendRoutingKey
                , payload);

    }
}
