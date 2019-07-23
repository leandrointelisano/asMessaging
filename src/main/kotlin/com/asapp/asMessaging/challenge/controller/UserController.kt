package com.asapp.asMessaging.challenge.controller

import com.asapp.asMessaging.challenge.exception.BadRequestException
import com.asapp.asMessaging.challenge.model.UserPostRequest
import com.asapp.asMessaging.challenge.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Response

/**
 * @class UserController handles the creation of each user and its requests connecting them with its service
 * @throws BadRequestException when the request body does not fit the user model
 */
class UserController(private val userService: UserService, private val objectMapper: ObjectMapper) {
    fun createUser() = { req: Request, _: Response ->
        try {
            objectMapper.readValue<UserPostRequest>(req.body(), UserPostRequest::class.java)

        } catch (e: Exception) {
            throw BadRequestException("Invalid request body while creating a system user")

        }?.let { user ->
            validatePasswordLength(user.password)
            userService.create(user).let { objectMapper.writeValueAsString(it) }
        }


    }

    private fun validatePasswordLength(password: String) {
        (password.length < MAX_PASSWORD_LENGTH).takeIf { !it }?.let { throw BadRequestException("Password length exceeds 16 chars") }
    }

    companion object {
        private const val MAX_PASSWORD_LENGTH = 16
    }


}