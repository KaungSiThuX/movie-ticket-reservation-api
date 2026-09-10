package com.kst.movie_ticket_reservation.util.pub_sub.subscriber;

import com.kst.movie_ticket_reservation.feat.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class SSESubscriber implements MessageListener
{
    private final SseService sseService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(@NonNull Message message, byte @Nullable [] pattern)
    {
        SeatEventMessage seatEventMessage = this.objectMapper.readValue(message.getBody(), SeatEventMessage.class);
        log.info("run subscriber name " + seatEventMessage.seatEventType().name());
        this.sseService.broadcast(seatEventMessage.seatEventType().name(), seatEventMessage.payload());
    }
}
