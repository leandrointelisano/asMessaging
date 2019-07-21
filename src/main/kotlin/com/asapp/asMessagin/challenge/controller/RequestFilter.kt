package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.exception.UnauthorizedException
import com.asapp.asMessagin.challenge.model.UserMessagePost
import com.asapp.asMessagin.challenge.service.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Filter
import spark.Request
import spark.Response

class RequestFilter(private val authenticationService: AuthenticationService, private val objectMapper: ObjectMapper) {

    fun handleToken() = Filter { req: Request, _: Response ->
        val userMessage = objectMapper.readValue<UserMessagePost>(req.body(), UserMessagePost::class.java)
        val token = requireNotNull(req.headers("token")) {"'token' header is required" }

        if (!validToken(token, userMessage.sender)) {
            throw UnauthorizedException("Invalid token")
        }
    }

    private fun validToken(token: String, userId: Int) =
        authenticationService.validToken(token, userId)


}