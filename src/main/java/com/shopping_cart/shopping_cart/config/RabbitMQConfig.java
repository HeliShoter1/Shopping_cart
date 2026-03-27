package com.shopping_cart.shopping_cart.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  
public class RabbitMQConfig {
    public static final String ORDER_QUEUE = "order.queue";
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String IMAGE_QUEUE = "image.queue";

    public static final String EXCHANGE = "shopping.exchange";

    public static final String ORDER_ROUTING_KEY = "order.created";
    public static final String EMAIL_ROUTING_KEY = "email.send";
    public static final String IMAGE_ROUTING_KEY = "image.process";

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue orderQueue(){
        return QueueBuilder.durable(ORDER_QUEUE).build();
    }

    @Bean
    public Queue emailQueue(){
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }

    @Bean
    public Queue imageQueue(){
        return QueueBuilder.durable(IMAGE_QUEUE).build();
    }

    @Bean
    public Binding orderBinding(){
        return BindingBuilder.bind(orderQueue())
                .to(exchange())
                .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding emailBinding(){
        return BindingBuilder.bind(emailQueue())
                .to(exchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding imageBinding(){
        return BindingBuilder.bind(imageQueue())
                .to(exchange())
                .with(IMAGE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory factory){
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
