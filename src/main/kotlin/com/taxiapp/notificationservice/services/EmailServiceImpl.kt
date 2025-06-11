package com.taxiapp.notificationservice.services

import com.taxiapp.notificationservice.dto.event.SendEmailRequestEvent
import com.taxiapp.notificationservice.dto.http.EmailSendRequestTO
import com.taxiapp.notificationservice.dto.http.ResultTO
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


@Service
class EmailServiceImpl(
    private val template: RabbitTemplate,
    private val emailQueue: Queue,
    @Value("\${mailing.appPassword}") private val appPassword: String,
    @Value("\${mailing.mailSender}") private val mailSender: String
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
        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"

        val auth: Authenticator = object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(mailSender, appPassword)
            }
        }
        val session: Session = Session.getInstance(props, auth)

        performSendEmail(session, event.recipientEmail, event.subject, event.body)
    }

    private fun performSendEmail(session: Session?, toEmail: String?, subject: String?, body: String?) {
        try {
            val msg = MimeMessage(session)
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8")
            msg.addHeader("format", "flowed")
            msg.addHeader("Content-Transfer-Encoding", "8bit")

            msg.setFrom(InternetAddress("no_reply@example.com", "NoReply-JD"))

            msg.replyTo = InternetAddress.parse("no_reply@example.com", false)

            msg.setSubject(subject, "UTF-8")

            msg.setText(body, "UTF-8")

            msg.sentDate = Date()

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false))
            Transport.send(msg)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}