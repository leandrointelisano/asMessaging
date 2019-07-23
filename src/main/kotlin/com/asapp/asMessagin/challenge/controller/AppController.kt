package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.persistence.Persistence
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Response

/**
 * @class {AppController}
 * Handles all the health check request and responds with an OK and the parsed object @HealthResponse into a Json
 */
class AppController(private val persistence: Persistence, private val objectMapper: ObjectMapper) {

    fun healthCheck() = { req: Request, res: Response ->
        persistence.healthy().takeIf { it }?.let { objectMapper.writeValueAsString(HealthResponse()) }
    }

}

data class HealthResponse(
    val health: String = "ok"
)