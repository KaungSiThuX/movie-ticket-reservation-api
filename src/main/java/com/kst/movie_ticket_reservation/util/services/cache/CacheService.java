package com.kst.movie_ticket_reservation.util.services.cache;

import java.time.Duration;
import java.util.Optional;

public interface CacheService
{
    public <T> void set(String key, T value, Duration ttl);

    public <T> Optional<T> getValue(String key, Class<T> clazz);

    public void delete(String key);

    public Long increaseKey(String key);

    public void expireKey(String key, Duration ttl);

    public String getString(String key);

    public void setString(String key, String value, Duration ttl);
}
