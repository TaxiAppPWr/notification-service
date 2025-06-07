package com.taxiapp.notificationservice.dto.http

data class PushSendRequestTO(
    val username: String,
    val title: String,
    val body: String,
)