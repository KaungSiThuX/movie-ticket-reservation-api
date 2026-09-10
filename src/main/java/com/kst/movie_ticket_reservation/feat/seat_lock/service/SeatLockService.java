package com.kst.movie_ticket_reservation.feat.seat_lock.service;

import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import com.kst.movie_ticket_reservation.util.exceptions.BadRequestException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatEventPayload;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatLockPayload;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatSoldPayload;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatUnlockPayload;

import java.util.List;
import java.util.Set;

public interface SeatLockService
{
    //    void lockSeats(SeatLockPayload seatLockPayload);
//
//    void unlockSeatsForLock(SeatUnlockPayload seatUnlockPayload) throws NotFoundException, BadRequestException;
//
    void unlockSeatsForSold(SeatSoldPayload seatSoldPayload);
//
//
//    boolean isLockedSeat(Long showTimeId, Long showSeatId) throws NotFoundException;
//
//    List<ShowSeatResDto> findLockedShowSeats(Long showTimeId) throws NotFoundException;
//
//    Set<Long> findLockedShowSeatIds(Long showTimeId);

    public boolean tryLockSeatAtomically(Long currentPersonId, Long showTimeId, List<Long> showSeatIds);

    public void releaseSeats(Long showTimeId, List<Long> showSeatIds);

    public Set<Long> getLockedSeatIdsForShowTime(Long showTimeId);
}
