package com.kst.movie_ticket_reservation.util.queue_job.producer;

public interface JobProducer<T>
{
    public void produce(T payload);
}
