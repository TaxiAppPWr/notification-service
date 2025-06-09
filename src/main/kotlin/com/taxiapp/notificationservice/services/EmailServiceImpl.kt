package com.taxiapp.notificationservice.services

import com.taxiapp.notificationservice.dto.event.SendEmailRequestEvent
import com.taxiapp.notificationservice.dto.http.EmailSendRequestTO
import com.taxiapp.notificationservice.dto.http.ResultTO
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    private val template: RabbitTemplate,
    private val emailQueue: Queue
) : EmailService {
    override fun pushEmailToQueue(emailRequest: EmailSendRequestTO): ResultTO {
        template.convertAndSend(
            emailQueue.name,
            SendEmailRequestEvent(
                recipientEmail = emailRequest.recipient,
                subject = emailRequest.subject,
                body = emailRequest.body
            )
        )
        return ResultTO(httpStatus = HttpStatus.CREATED)
    }

    override fun sendEmail(event: SendEmailRequestEvent) {
        println("Email sending not implemented")
        // TODO: Implement email sending logic here - e.g. MailTrap
    }
}