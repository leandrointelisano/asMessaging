package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.persistence.UserPersistence
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Response

class AppController (private val userPersistence: UserPersistence, private val objectMapper: ObjectMapper){

    fun healthCheck()  = { req: Request, res: Response ->
        userPersistence.healthy().takeIf { it }?.let { objectMapper.writeValueAsString(HealthResponse()) }
    }

}

data class HealthResponse(
    val health: String = "ok"
)