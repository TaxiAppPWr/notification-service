package com.taxiapp.notificationservice.services

import com.taxiapp.notificationservice.dto.event.SendEmailRequestEvent
import com.taxiapp.notificationservice.dto.http.EmailSendRequestTO
import com.taxiapp.notificationservice.dto.http.ResultTO

interface EmailService {
    fun pushEmailToQueue(emailRequest: EmailSendRequestTO): ResultTO

    fun sendEmail(event: SendEmailRequestEvent): ResultTO
}