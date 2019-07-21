package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.exception.BadRequestException
import com.asapp.asMessagin.challenge.model.UserPostRequest
import com.asapp.asMessagin.challenge.service.UserService
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
            userService.create(user).let { objectMapper.writeValueAsString(it) }
        }


    }


}