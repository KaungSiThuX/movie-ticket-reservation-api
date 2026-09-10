package com.kst.movie_ticket_reservation.util.queue_job.consumer.impl;

import com.kst.movie_ticket_reservation.integration.rabbit_mq.config.RabbitMQConfig;
import com.kst.movie_ticket_reservation.util.exceptions.CustomS3Exception;
import com.kst.movie_ticket_reservation.util.job_payloads.ImageDeletePayload;
import com.kst.movie_ticket_reservation.util.queue_job.consumer.JobConsumer;
import com.kst.movie_ticket_reservation.util.services.object_storage.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageDeleteJobConsumer implements JobConsumer<ImageDeletePayload>
{
    private final ObjectStorageService objectStorageService;

    @Override
    @RabbitListener(queues = RabbitMQConfig.imageDeleteQueueName)
    public void process(ImageDeletePayload payload) throws URISyntaxException, CustomS3Exception
    {
        log.info("image url in job consumer " + payload.imageUrl());
        this.objectStorageService.delete(payload.imageUrl());
    }
}
