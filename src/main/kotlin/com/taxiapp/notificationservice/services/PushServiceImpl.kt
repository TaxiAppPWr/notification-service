package com.taxiapp.notificationservice.services

import aws.sdk.kotlin.services.sns.SnsClient
import aws.sdk.kotlin.services.sns.model.CreatePlatformEndpointRequest
import aws.sdk.kotlin.services.sns.model.PublishRequest
import com.taxiapp.notificationservice.dto.event.SendPushRequestEvent
import com.taxiapp.notificationservice.dto.http.PushSendRequestTO
import com.taxiapp.notificationservice.dto.http.ResultTO
import com.taxiapp.notificationservice.entity.SnsEndpointEntity
import com.taxiapp.notificationservice.repository.SnsEndpointRepository
import kotlinx.coroutines.runBlocking
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class PushServiceImpl(
    private val snsEndpointRepository: SnsEndpointRepository,
    private val snsClient: SnsClient,
    private val template: RabbitTemplate,
    private val pushQueue: Queue,
    @Value("\${aws.sns.platformapplication.arn}") private val platformApplicationArn: String
) : PushService {

    private suspend fun createEndpointOnSns(token: String): String? {
        val request = CreatePlatformEndpointRequest {
            platformApplicationArn = this@PushServiceImpl.platformApplicationArn
            this.token = token
        }
        return snsClient.createPlatformEndpoint(request).endpointArn
    }

    override fun createEndpoint(
        token: String,
        username: String
    ): ResultTO {
        if (snsEndpointRepository.existsByUsernameAndToken(username, token)) {
            return ResultTO(httpStatus = HttpStatus.ALREADY_REPORTED)
        }
        var endpointArn: String?
        runBlocking {
            endpointArn = createEndpointOnSns(token)
        }
        if (endpointArn == null) {
            return ResultTO(httpStatus = HttpStatus.INTERNAL_SERVER_ERROR, messages = listOf("Failed to create SNS endpoint"))
        }

        snsEndpointRepository.save(
            SnsEndpointEntity(
                username = username,
                token = token,
                endpointArn = endpointArn
            )
        )
        return ResultTO(
            httpStatus = HttpStatus.CREATED,
            messages = listOf("SNS endpoint created successfully")
        )
    }

    override fun sendPushToQueue(request: PushSendRequestTO): ResultTO {
        template.convertAndSend(pushQueue.name,
            SendPushRequestEvent(
                username = request.username,
                title = request.title,
                body = request.body,
            )
        )
        return ResultTO(
            httpStatus = HttpStatus.CREATED,
            messages = listOf("Push request sent to queue successfully")
        )
    }

    override fun sendPush(event: SendPushRequestEvent) {
        val endpoints = snsEndpointRepository.findAllByUsername(event.username)
        if (endpoints.isEmpty()) {
            return
        }
        val payload = """{
          "fcmV1Message": {
            "message": {
              "data": {
                "title": "${event.title}",
                "body": "${event.body}"
              }
            }
          }
        }""".trimIndent()
        runBlocking {
            endpoints.forEach { endpoint ->
                val publishRequest = PublishRequest {
                    targetArn = endpoint.endpointArn
                    messageStructure = "json"
                    message = """{"GCM":${payload.trim()}}"""
                }
                snsClient.publish(publishRequest)
            }
        }
    }
}