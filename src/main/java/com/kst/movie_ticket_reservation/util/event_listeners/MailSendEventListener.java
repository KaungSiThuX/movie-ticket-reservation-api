package com.kst.movie_ticket_reservation.util.event_listeners;

import com.kst.movie_ticket_reservation.util.events.ImageDeleteEvent;
import com.kst.movie_ticket_reservation.util.events.MailSendEvent;
import com.kst.movie_ticket_reservation.util.queue_job.producer.impl.MailSendJobProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSendEventListener
{
    private final MailSendJobProducer mailSendJobProducer;

    @Async
    @EventListener
    public void handleMailSendEvent(MailSendEvent mailSendEvent)
    {
        this.mailSendJobProducer.produce(mailSendEvent.mailSendPayload());
    }
}
