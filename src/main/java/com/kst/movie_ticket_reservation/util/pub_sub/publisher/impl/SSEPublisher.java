package com.kst.movie_ticket_reservation.util.pub_sub.publisher.impl;

import com.kst.movie_ticket_reservation.integration.redis.service.RedisService;
import com.kst.movie_ticket_reservation.util.pub_sub.publisher.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SSEPublisher implements Publisher
{
    private final RedisService redisService;

    @Override
    public void publish(String topic, Object message)
    {
        System.out.println("run publisher" + topic);
        System.out.println("msg in publisher " + message);
        this.redisService.convertAndSend(topic, message);
    }
}
