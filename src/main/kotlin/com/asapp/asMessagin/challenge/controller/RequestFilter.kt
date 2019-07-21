package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.exception.UnauthorizedException
import com.asapp.asMessagin.challenge.model.UserMessagePost
import com.asapp.asMessagin.challenge.service.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Filter
import spark.Request
import spark.Response

class RequestFilter(private val authenticationService: AuthenticationService, private val objectMapper: ObjectMapper) {

    fun handleToken() = Filter { req: Request, res: Response ->
        val userMessage = objectMapper.readValue<UserMessagePost>(req.body(), UserMessagePost::class.java)

        validateToken(
            req.headers("token"), userMessage.sender
            )
    }

    private fun validateToken(token: String?, userId: Int) {
        requireNotNull(token) { "'token' header is required" }
        authenticationService.validToken(token, userId).takeIf { it } ?: throw UnauthorizedException("Invalid token")
    }

}