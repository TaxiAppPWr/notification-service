package com.taxiapp.notificationservice.repository

import com.taxiapp.notificationservice.entity.SnsEndpointEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SnsEndpointRepository : JpaRepository<SnsEndpointEntity, Long> {

    fun existsByUsernameAndToken(username: String, token: String): Boolean

    fun findAllByUsername(username: String,): List<SnsEndpointEntity>

}