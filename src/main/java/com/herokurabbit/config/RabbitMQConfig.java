package com.herokurabbit.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.herokurabbit.service.RabbitMQListener;

@Configuration
public class RabbitMQConfig {

	@Value("${heroku.rabbitmq.queue}")
	String queueName;
	
	@Value("${heroku.rabbitmq.uri}")
	String rabbitUri;

	@Bean
	Queue queue() {
		return new Queue(queueName, true);
	}

	//create MessageListenerContainer using default connection factory
	@Bean
	MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueues(queue());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQListener());
		return simpleMessageListenerContainer;

	}

	//create custom connection factory
	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUri(rabbitUri);
		return connectionFactory;
	}
}
