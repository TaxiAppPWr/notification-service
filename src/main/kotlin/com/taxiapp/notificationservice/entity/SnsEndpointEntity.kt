package com.taxiapp.notificationservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity(name = "sns_endpoint")
@Table(indexes = [
    Index(name = "idx_sns_endpoint_username_token", columnList = "username, token", unique = true)
])
class SnsEndpointEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column
    var username: String,

    @Column
    var token: String,

    @Column
    var endpointArn: String,
)