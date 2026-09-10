package com.kst.movie_ticket_reservation.integration.rabbit_mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{
    public static final String topicExchangeName = "ticket.exchange";

    // later need to improve dynamic

    public static final String imageDeleteQueueName = "image.queue.delete";
    public static final String imageDeleteRoutingKey = "image.delete.routing.key.";

    public static final String mailSendQueueName = "mail.send.queue";
    public static final String mailSendRoutingKey = "mail.send.routing.key";

    @Bean
    Queue imageDeleteQueue()
    {
        return new Queue(imageDeleteQueueName, true, false, false);
    }

    @Bean
    Binding imageDeleteBinding(Queue imageDeleteQueue, TopicExchange topicExchange)
    {
        return BindingBuilder.bind(imageDeleteQueue).to(topicExchange).with(imageDeleteRoutingKey);
    }

    @Bean
    Queue mailSendQueue()
    {
        return new Queue(mailSendQueueName, true, false, false);
    }

    @Bean
    Binding mailSendBinding(Queue mailSendQueue, TopicExchange topicExchange)
    {
        return BindingBuilder.bind(mailSendQueue).to(topicExchange).with(mailSendRoutingKey);
    }

    @Bean
    TopicExchange topicExchange()
    {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    MessageConverter messageConverter()
    {
        return new JacksonJsonMessageConverter();
    }

}
