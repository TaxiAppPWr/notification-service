package com.taxiapp.notificationservice.controllers

import com.taxiapp.notificationservice.dto.http.EmailSendRequestTO
import com.taxiapp.notificationservice.services.EmailService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}