package com.kst.movie_ticket_reservation.util.event_listeners;

import com.kst.movie_ticket_reservation.util.events.ImageDeleteEvent;
import com.kst.movie_ticket_reservation.util.queue_job.producer.impl.ImageDeleteJobProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ImageDeleteEventListener
{
    private final ImageDeleteJobProducer imageDeleteJobProducer;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleImageDeleteEvent(ImageDeleteEvent imageDeleteEvent)
    {
        this.imageDeleteJobProducer.produce(imageDeleteEvent.imageDeletePayload());
    }
}
