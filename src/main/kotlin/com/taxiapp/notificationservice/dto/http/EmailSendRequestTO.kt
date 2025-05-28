package com.taxiapp.notificationservice.dto.http

data class EmailSendRequestTO(
    val recipient: String,
    val subject: String,
    val body: String,
)