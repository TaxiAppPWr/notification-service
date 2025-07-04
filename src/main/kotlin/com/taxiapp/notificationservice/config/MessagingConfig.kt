package com.taxiapp.notificationservice.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessagingConfig(
    @Value("\${rabbit.queue.email.name}") private val emailQueueName: String,
    @Value("\${rabbit.queue.push.name}") private val pushQueueName: String
) {


    @Bean
    fun emailQueue(): Queue {
        return QueueBuilder.durable(emailQueueName).build()
    }

    @Bean
    fun pushQueue(): Queue {
        return QueueBuilder.durable(pushQueueName).build()
    }

}