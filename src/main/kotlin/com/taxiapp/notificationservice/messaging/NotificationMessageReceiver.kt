package com.taxiapp.notificationservice.messaging

import com.taxiapp.notificationservice.dto.event.SendEmailRequestEvent
import com.taxiapp.notificationservice.dto.event.SendPushRequestEvent
import com.taxiapp.notificationservice.services.EmailService
import com.taxiapp.notificationservice.services.PushService
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@RabbitListener(queues = ["\${rabbit.queue.email.name}", "\${rabbit.queue.push.name}"])
@Component
class NotificationMessageReceiver(
    private val emailService: EmailService,
    private val pushService: PushService
) {
    @RabbitHandler
    fun receiveSendEmailRequestEvent(event: SendEmailRequestEvent) {
        emailService.sendEmail(event)
    }

    @RabbitHandler
    fun receivePushNotificationEvent(event: SendPushRequestEvent) {
        pushService.sendPush(event)
    }

}