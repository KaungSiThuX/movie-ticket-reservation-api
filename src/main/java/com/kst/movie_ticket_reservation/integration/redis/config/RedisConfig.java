package com.kst.movie_ticket_reservation.integration.redis.config;

import com.kst.movie_ticket_reservation.util.pub_sub.subscriber.SSESubscriber;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig
{
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
    {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer myRedisCacheManagerBuilderCustomizer()
    {
        // 1. Use the new RedisSerializer static methods
        RedisSerializer<String> keySerializer = RedisSerializer.string();
        RedisSerializer<Object> valueSerializer = RedisSerializer.json();

        // 2. Build the Base Configuration
        RedisCacheConfiguration baseConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .disableCachingNullValues();

        return builder -> builder
                .withCacheConfiguration("rate-limit-buckets", baseConfig.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration("genres", baseConfig.entryTtl(Duration.ofSeconds(30)))
                .withCacheConfiguration("theatres", baseConfig.entryTtl(Duration.ofSeconds(30)))
                .withCacheConfiguration("seats", baseConfig.entryTtl(Duration.ofSeconds(30)));
    }

    @Bean
    public ChannelTopic channelTopic()
    {
        return new ChannelTopic("SeatEventTopic");
    }

    @Bean
    public MessageListenerAdapter sseListenerAdapter(SSESubscriber sseSubscriber)
    {
        return new MessageListenerAdapter(sseSubscriber);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory,
                                                        MessageListenerAdapter messageListenerAdapter,
                                                        ChannelTopic channelTopic)
    {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, channelTopic);
        return container;
    }
}
