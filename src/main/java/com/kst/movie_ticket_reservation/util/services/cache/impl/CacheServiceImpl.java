package com.kst.movie_ticket_reservation.util.services.cache.impl;

import com.kst.movie_ticket_reservation.integration.redis.service.RedisService;
import com.kst.movie_ticket_reservation.util.services.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService
{
    private final RedisService redisService;

    @Override
    public <T> void set(String key, T value, Duration ttl)
    {
        this.redisService.set(key, value, ttl);
    }

    @Override
    public <T> Optional<T> getValue(String key, Class<T> clazz)
    {
        return this.redisService.get(key, clazz);
    }

    @Override
    public void delete(String key)
    {
        this.redisService.delete(key);
    }

    @Override
    public Long increaseKey(String key)
    {
        return this.redisService.increaseKey(key);
    }

    @Override
    public void expireKey(String key, Duration ttl)
    {
        this.redisService.expire(key, ttl);
    }

    @Override
    public String getString(String key)
    {
        return this.redisService.getString(key);
    }

    @Override
    public void setString(String key, String value, Duration ttl)
    {
        this.redisService.setString(key, value, ttl);
    }

}
