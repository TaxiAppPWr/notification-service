package com.taxiapp.notificationservice.controllers

import com.taxiapp.notificationservice.dto.http.EmailSendRequestTO
import com.taxiapp.notificationservice.services.EmailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notification")
class NotificationController(
    private val emailService: EmailService
) {
    @PostMapping("/email")
    fun sendEmail(@RequestBody emailRequest: EmailSendRequestTO) : ResponseEntity<Any> {
        val result = emailService.pushEmailToQueue(emailRequest)

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