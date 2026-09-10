package com.kst.movie_ticket_reservation.feat.seat_lock.service.impl;

import com.kst.movie_ticket_reservation.feat.seat_lock.service.SeatLockService;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.integration.redis.service.RedisService;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatLockPayload;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatSoldPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatLockServiceImpl implements SeatLockService
{
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisService redisService;
    // Atomic Lua Script: Lock all seats or none
    private static final String SEAT_LOCK_KEY = "seat:lock:";
    private static final String SEAT_LOCK_SHOW_TIME_KEY = "showtime:locked_seats:";
    private static final String LOCK_SEATS_LUA =
            "local userId = ARGV[1] " +
                    "local ttl = tonumber(ARGV[2]) " +
                    "local showTimeSetKey = ARGV[3] " +
                    "for i, key in ipairs(KEYS) do " +
                    "   if redis.call('EXISTS', key) == 1 then " +
                    "       return redis.call('GET', key) " +
                    "   end " +
                    "end " +
                    "for i, key in ipairs(KEYS) do " +
                    "   redis.call('SETEX', key, ttl, userId) " +
                    "   redis.call('SADD', showTimeSetKey, key) " +
                    "end " +
                    "redis.call('EXPIRE', showTimeSetKey, ttl) " +
                    "return 'SUCCESS'";

    @Override
    public boolean tryLockSeatAtomically(Long currentPersonId, Long showTimeId, List<Long> showSeatIds)
    {
        log.info("Attempting lock -> showTimeId: {}, showSeatIds: {}", showTimeId, showSeatIds);
        List<String> keys = showSeatIds.stream()
                .map(id -> SEAT_LOCK_KEY + id)
                .toList();

        String showTimeSetKey = SEAT_LOCK_SHOW_TIME_KEY + showTimeId;
        log.info("Target Redis Set Key: {}", showTimeSetKey);
        long ttlSeconds = 900; //15 * 60; // 15 minutes

        RedisScript<String> script = new DefaultRedisScript<>(LOCK_SEATS_LUA, String.class);

        String result = this.stringRedisTemplate.execute(script, keys, currentPersonId.toString(),
                String.valueOf(ttlSeconds),
                showTimeSetKey);

        return "SUCCESS".equals(result);
    }

    @Override
    public void releaseSeats(Long showTimeId, List<Long> showSeatIds)
    {
        List<String> keys = showSeatIds.stream()
                .map(id -> SEAT_LOCK_KEY + id)
                .toList();

        if (!keys.isEmpty())
        {
            this.stringRedisTemplate.delete(keys);
        }

        String showTimeSetKey = SEAT_LOCK_SHOW_TIME_KEY + showTimeId;
        keys.forEach(key -> this.stringRedisTemplate.opsForSet().remove(showTimeSetKey, key));
    }

    @Override
    public Set<Long> getLockedSeatIdsForShowTime(Long showTimeId)
    {
        String showTimeSetKey = SEAT_LOCK_SHOW_TIME_KEY + showTimeId;
        Set<String> lockedKeys = this.stringRedisTemplate.opsForSet().members(showTimeSetKey);
        System.out.println("Raw Redis Set Members: " + lockedKeys);
        if (lockedKeys == null || lockedKeys.isEmpty())
        {
            return Set.of();
        }

        return lockedKeys.stream()
                .map(key -> key.replace(SEAT_LOCK_KEY, ""))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

//    @Override
//    public void lockSeats(SeatLockPayload seatLockPayload)
//    {
//        String showTimeLockKey = "showTime:" + seatLockPayload.showTimeId();
//        for (ShowSeatResDto showSeatResDto : seatLockPayload.showSeatList())
//        {
//
//        }
//
//    }
//
//    @Override
//    public void unlockSeatsForLock(SeatUnlockPayload seatUnlockPayload) throws NotFoundException, BadRequestException
//    {

    /// /        ShowTime existingShowTime =
    /// /                this.showTimeRepository.findById(seatUnlockPayload.showTimeId())
    /// /                        .orElseThrow(() -> new NotFoundException("show time not found"));
    /// /
    /// /        List<ShowSeat> showSeatList =
    /// /                this.showSeatRepository.findAllById(seatUnlockPayload.showSeatList().stream().map
    /// /                (ShowSeatResDto::id).toList());
    /// /
    /// /        if (showSeatList.size() != seatUnlockPayload.showSeatList().size())
    /// /        {
    /// /            throw new BadRequestException("one or more seat are not available");
    /// /        }
//
//        //  String seatLockKey = SEAT_LOCK_KEY + existingShowTime.getId();
//
//        for (ShowSeatResDto showSeatResDto : seatUnlockPayload.showSeatList())
//        {
//            String seatLockKey = SEAT_LOCK_KEY + seatUnlockPayload.showTimeId() + "_showSeat_" + showSeatResDto.id();
//
//
//            this.redisService.delete(seatLockKey);
//        }
//    }
//
    @Override
    public void unlockSeatsForSold(SeatSoldPayload seatSoldPayload)
    {
        List<Long> showSeatIds = seatSoldPayload.showSeatList().stream().map(ShowSeatResDto::id).toList();

        this.releaseSeats(seatSoldPayload.showTimeId(), showSeatIds);
    }
//
//    @Override
//    public boolean isLockedSeat(Long showTimeId, Long showSeatId)
//    {
//        String seatLockKey = SEAT_LOCK_KEY + showTimeId + "_showTime_" + showSeatId;
//
////        return this.redisService.get(seatLockKey, SeatLockPayload.class)
////                .map(SeatLockPayload::showSeatList)
////                .map(showSeats -> showSeats.stream()
////                        .anyMatch(showSeatResDto -> Objects.equals(showSeatResDto.id(), showSeatId)))
////                .orElse(false);
//
//        Optional<ShowSeatResDto> lockedShowSeatResDto = this.redisService.get(seatLockKey, ShowSeatResDto.class);
//
//        return lockedShowSeatResDto.isPresent();
//    }
//
//    @Override
//    public List<ShowSeatResDto> findLockedShowSeats(Long showTimeId) throws NotFoundException
//    {
//        String seatLockKey = SEAT_LOCK_KEY + showTimeId;
//
//        return this.redisService.get(seatLockKey, SeatLockPayload.class)
//                .map(SeatLockPayload::showSeatList)
//                .orElseGet(List::of);
//    }
//
//    @Override
//    public Set<Long> findLockedShowSeatIds(Long showTimeId)
//    {
//        String seatLockKey = SEAT_LOCK_KEY + showTimeId;
//
//        Optional<SeatLockPayload> seatLockPayload = this.redisService.get(seatLockKey, SeatLockPayload.class);
//
//        return seatLockPayload.map(lockPayload -> lockPayload.showSeatList().stream().map(ShowSeatResDto::id)
//        .collect(Collectors.toSet())).orElseGet(Set::of);
//    }
//
//
////    private String generateRawPayloadKey(List<ShowSeat> showSeatList, Long currentPersonId, Long showTimeId)
////    {
////        String sortedShowSeatIds =
////                showSeatList.stream().map(ShowSeat::getId).sorted().toList()
////                        .stream().map(Object::toString).collect(Collectors.joining(","));
////
////        return String.format("user%d|showTime%d|showSeats:%s", currentPersonId,
////                showTimeId, sortedShowSeatIds);
////    }
}
