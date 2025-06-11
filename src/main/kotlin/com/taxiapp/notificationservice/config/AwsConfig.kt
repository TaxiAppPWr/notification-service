package com.taxiapp.notificationservice.config

import aws.sdk.kotlin.services.sns.SnsClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfig(
    @Value("\${aws.region:us-east-1}") private val awsRegion: String
) {
    @Bean
    fun snsClient(): SnsClient {
        return SnsClient {
            region = awsRegion
        }
    }
}