package com.kst.movie_ticket_reservation.integration.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisService
{
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void set(String key, T value, Duration ttl)
    {
        String jsonString = this.objectMapper.writeValueAsString(value);
        this.stringRedisTemplate.opsForValue().set(key, jsonString, ttl);
    }

    public String getString(String key)
    {
        return this.stringRedisTemplate.opsForValue().get(key);
    }

    public void setString(String key, String value, Duration ttl)
    {
        this.stringRedisTemplate.opsForValue().set(key, value, ttl);
    }


    public <T> Optional<T> get(String key, Class<T> clazz)
    {
        try
        {
            String jsonString = this.stringRedisTemplate.opsForValue().get(key);

            if (jsonString == null || jsonString.isEmpty())
            {
                return Optional.empty();
            }

            return Optional.of(this.objectMapper.readValue(jsonString, clazz));

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

//    public <T> Optional<List<T>> getList(String key, TypeReference<List<T>> typeReference)
//    {
//        try
//        {
//            String jsonString = this.stringRedisTemplate.opsForValue().get(key);
//
//            if (jsonString == null || jsonString.isEmpty())
//            {
//                return Optional.empty();
//            }
//            return Optional.of(this.objectMapper.readValue(jsonString, typeReference));
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//    }

    public void delete(String key)
    {
        this.stringRedisTemplate.delete(key);
    }

    public Long increaseKey(String key)
    {
        return this.stringRedisTemplate.opsForValue().increment(key);
    }

    public void expire(String key, Duration ttl)
    {
        this.stringRedisTemplate.expire(key, ttl);
    }

    public void convertAndSend(String topic, Object message)
    {
        this.stringRedisTemplate.convertAndSend(topic, message);
    }
}
