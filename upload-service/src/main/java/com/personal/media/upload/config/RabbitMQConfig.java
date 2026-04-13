package com.personal.media.upload.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PHOTO_EXCHANGE = "photo.upload.exchange";
    public static final String PHOTO_QUEUE = "photo.upload.queue";
    public static final String PHOTO_ROUTING_KEY = "photo.upload.key";

    @Bean
    public TopicExchange photoExchange() {
        return new TopicExchange(PHOTO_EXCHANGE);
    }

    @Bean
    public Queue photoQueue() {
        return new Queue(PHOTO_QUEUE, false);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(PHOTO_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
