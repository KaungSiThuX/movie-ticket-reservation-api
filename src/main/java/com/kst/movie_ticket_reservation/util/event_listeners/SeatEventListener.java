package com.kst.movie_ticket_reservation.util.event_listeners;

import com.kst.movie_ticket_reservation.feat.seat_lock.service.SeatLockService;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.feat.sse.service.SseService;
import com.kst.movie_ticket_reservation.util.enums.SeatEventType;
import com.kst.movie_ticket_reservation.util.events.SeatEvent;
import com.kst.movie_ticket_reservation.util.events.SeatLockEvent;
import com.kst.movie_ticket_reservation.util.events.SeatSoldEvent;
import com.kst.movie_ticket_reservation.util.events.SeatUnlockEvent;
import com.kst.movie_ticket_reservation.util.exceptions.BadRequestException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatEventListener
{
    private final SseService sseService;
    private final SeatLockService seatLockService;

    @EventListener
    public void handleSeatLockEvent(SeatLockEvent seatLockEvent)
    {
        this.sseService.publish(SeatEventType.SEAT_LOCK, seatLockEvent.seatLockPayload());
        this.seatLockService.tryLockSeatAtomically(seatLockEvent.seatLockPayload().currentPersonId(),
                seatLockEvent.seatLockPayload().showTimeId(),
                seatLockEvent.seatLockPayload().showSeatList().stream().map(ShowSeatResDto::id).toList());
    }

    @EventListener
    public void handleSeatUnlockEvent(SeatUnlockEvent seatUnlockEvent) throws NotFoundException, BadRequestException
    {

        this.sseService.publish(SeatEventType.SEAT_UNLOCK, seatUnlockEvent.seatUnlockPayload());
        //  this.seatLockService.unlockSeatsForLock(seatUnlockEvent.seatUnlockPayload());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSeatSoldEvent(SeatSoldEvent seatSoldEvent) throws NotFoundException, BadRequestException
    {
        log.info("run handle seat sold event in listener ");
        this.sseService.publish(SeatEventType.SEAT_SOLD, seatSoldEvent.seatSoldPayload());
        this.seatLockService.unlockSeatsForSold(seatSoldEvent.seatSoldPayload());
    }
}
