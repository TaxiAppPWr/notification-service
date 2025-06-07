package com.taxiapp.notificationservice.services

import com.taxiapp.notificationservice.dto.event.SendPushRequestEvent
import com.taxiapp.notificationservice.dto.http.PushSendRequestTO
import com.taxiapp.notificationservice.dto.http.ResultTO

interface PushService {

    fun createEndpoint(token: String, username: String): ResultTO

    fun sendPushToQueue(request: PushSendRequestTO): ResultTO

    fun sendPush(event: SendPushRequestEvent)
}