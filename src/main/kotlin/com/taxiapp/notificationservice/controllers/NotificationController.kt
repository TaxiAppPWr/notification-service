package com.taxiapp.notificationservice.controllers

import com.taxiapp.notificationservice.dto.http.EmailSendRequestTO
import com.taxiapp.notificationservice.dto.http.PushSendRequestTO
import com.taxiapp.notificationservice.dto.http.TokenRequest
import com.taxiapp.notificationservice.services.EmailService
import com.taxiapp.notificationservice.services.PushService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notification")
class NotificationController(
    private val emailService: EmailService,
    private val pushService: PushService,
) {
    @PostMapping("/user/token")
    fun registerUserToken(@RequestBody request: TokenRequest, @RequestHeader("username") username: String): ResponseEntity<Any> {
        val result = pushService.createEndpoint(request.token, username)
        return ResponseEntity.status(result.httpStatus).body(result.messages)
    }

    @PostMapping("/email")
    fun sendEmail(@RequestBody emailRequest: EmailSendRequestTO) : ResponseEntity<Any> {
        val result = emailService.pushEmailToQueue(emailRequest)
        return if (result.isSuccess()) {
            ResponseEntity.ok(null)
        } else {
            ResponseEntity.status(result.httpStatus).body(result.messages)
        }
    }

    @PostMapping("/push")
    fun sendPushNotification(@RequestBody notification: PushSendRequestTO): ResponseEntity<Any> {
        val result = pushService.sendPushToQueue(notification)
        return if (result.isSuccess()) {
            ResponseEntity.ok(null)
        } else {
            ResponseEntity.status(result.httpStatus).body(result.messages)
        }
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "UP",
            "service" to "notification-service"
        ))
    }
}