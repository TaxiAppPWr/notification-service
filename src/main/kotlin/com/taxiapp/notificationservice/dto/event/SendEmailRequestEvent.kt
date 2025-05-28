package com.taxiapp.notificationservice.dto.event

data class SendEmailRequestEvent(
    val recipientEmail: String,
    val subject: String,
    val body: String
)
