package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.exception.BadRequestException
import com.asapp.asMessagin.challenge.model.UserPostRequest
import com.asapp.asMessagin.challenge.service.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Response

/**
 * @class {com.asapp.asMessagin.challenge.controller.AuthenticationController}
 * Handles the logIn and logOut requests. It connects the service with its route.
 * @throws {BadRequestException} when request body does not fit the correct object
 */
class AuthenticationController(
    private val authenticationService: AuthenticationService,
    private val objectMapper: ObjectMapper
) {
    fun login() = { req: Request, res: Response ->
        try {
            objectMapper.readValue<UserPostRequest>(req.body(), UserPostRequest::class.java)
        } catch (e: Exception) {
            throw BadRequestException("Invalid request body when logging in")
        }.let { user ->
            authenticationService.loginUser(user).let { objectMapper.writeValueAsString(it) }
        }


    }

    fun logout() = { req: Request, res: Response ->
        try {
            objectMapper.readValue<UserPostRequest>(req.body(), UserPostRequest::class.java)
        } catch (e: Exception) {
            throw BadRequestException("Invalid request body when logging out")
        }.let { user ->
            authenticationService.logoutUser(user).let { objectMapper.writeValueAsString(it) }
        }
    }
}