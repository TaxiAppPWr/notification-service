package com.taxiapp.notificationservice.config

import aws.sdk.kotlin.services.sns.SnsClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfig {
    @Bean
    fun snsClient(): SnsClient {
        return SnsClient.builder().build()
    }
}