package com.kst.movie_ticket_reservation.util.queue_job.consumer;

import java.net.URISyntaxException;

public interface JobConsumer<T>
{
    public void process(T payload) throws Exception;
}
