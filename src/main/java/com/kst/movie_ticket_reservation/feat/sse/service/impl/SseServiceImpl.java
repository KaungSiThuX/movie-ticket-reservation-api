package com.kst.movie_ticket_reservation.feat.sse.service.impl;

import com.kst.movie_ticket_reservation.feat.sse.service.SseService;
import com.kst.movie_ticket_reservation.util.enums.SeatEventType;
import com.kst.movie_ticket_reservation.util.pub_sub.publisher.impl.SSEPublisher;
import com.kst.movie_ticket_reservation.util.pub_sub.subscriber.SeatEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService
{
    private final SSEPublisher ssePublisher;
    private final List<SseEmitter> sseEmitterList = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;

    @Async
    @Override
    public void broadcast(String eventName, Object payload)
    {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        System.out.println("emitter size " + sseEmitterList.size());
        System.out.println("event name in broadcast is " + eventName);


        for (SseEmitter emitter : sseEmitterList)
        {
            System.out.println("run for loop");

            try
            {
                // Send the SSE event
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(this.objectMapper.writeValueAsString(payload)));
            }
            catch (Exception e)
            {
                // Mark failed emitter for removal (client closed connection)
                deadEmitters.add(emitter);
                emitter.complete();
                log.error("sse broadcast error " + e);
            }
        }

        this.sseEmitterList.removeAll(deadEmitters);
    }

//    @Override
//    public void publish(SeatEventType seatEventType, Object payload)
//    {
//        SeatEventMessage seatEventMessage = new SeatEventMessage(seatEventType, payload);
//        this.ssePublisher.publish("SeatEventTopic", this.objectMapper.writeValueAsString(seatEventMessage));
//    }

    @Override
    public void publish(SeatEventType seatEventType, Object payload)
    {
        SeatEventMessage seatEventMessage = new SeatEventMessage(seatEventType, payload);
        this.ssePublisher.publish("SeatEventTopic", this.objectMapper.writeValueAsString(seatEventMessage));
    }

    @Override
    public SseEmitter subscribe()
    {
        // Set timeout to 30 minutes (0L or null for infinite timeout)
        SseEmitter emitter = new SseEmitter(1_800_000L);

        // Clean up when client disconnects or times out
        emitter.onCompletion(() -> this.sseEmitterList.remove(emitter));
        emitter.onTimeout(() ->
        {
            emitter.complete();
            this.sseEmitterList.remove(emitter);
        });
        emitter.onError((e) -> this.sseEmitterList.remove(emitter));

        this.sseEmitterList.add(emitter);
        System.out.println("emitter in service " + emitter);

        try
        {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("connected"));
        }
        catch (IOException e)
        {
            this.sseEmitterList.remove(emitter);
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
