package com.taxiapp.notificationservice.dto.event

data class SendPushRequestEvent(
    val username: String,
    val title: String,
    val body: String,
)
