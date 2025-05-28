package com.taxiapp.notificationservice.messaging

import com.taxiapp.notificationservice.dto.event.SendEmailRequestEvent
import com.taxiapp.notificationservice.services.EmailService
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@RabbitListener(queues = ["\${rabbit.queue.email.name}"])
@Component
class NotificationMessageReceiver(
    private val emailService: EmailService
) {
    @RabbitHandler
    fun receiveSendEmailRequestEvent(event: SendEmailRequestEvent) {
        emailService.sendEmail(event)
    }

}