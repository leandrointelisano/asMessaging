package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.exception.BadRequestException
import com.asapp.asMessagin.challenge.model.UserPostRequest
import com.asapp.asMessagin.challenge.service.LogInService
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Response

class LogInController(private val logInService: LogInService, private val objectMapper: ObjectMapper) {
    fun login() = { req: Request, res: Response ->
        try {
            objectMapper.readValue<UserPostRequest>(req.body(), UserPostRequest::class.java)
        } catch (e: Exception) {
            throw BadRequestException("Invalid request body")
        }.let { user ->
            logInService.loginUser(user).let { objectMapper.writeValueAsString(it) }
        }



    }

    fun logout() = {
        req: Request, res: Response ->
        try {
            objectMapper.readValue<UserPostRequest>(req.body(), UserPostRequest::class.java)
        }catch (e: Exception){
            throw BadRequestException("Invalid request body when logging out")
        }.let { user->
            logInService.logoutUser(user).let { objectMapper.writeValueAsString(it) }
        }
    }
}