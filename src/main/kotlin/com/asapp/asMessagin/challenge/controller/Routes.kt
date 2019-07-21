package com.asapp.asMessagin.challenge.controller

import com.fasterxml.jackson.databind.ObjectMapper
import spark.Spark.*

class Routes(
    private val authenticationController: AuthenticationController,
    private val userController: UserController,
    private val objectMapper: ObjectMapper,
    private val requestFilter: RequestFilter,
    private val messagingController: MessagingController
) {
    fun register() {
        post("/users", userController.createUser())
        post("/login", authenticationController.login())
        post("/logout", authenticationController.logout())
        before("/messages", requestFilter.handleToken())
        post("/messages", messagingController.sendMessage())
        get("/messages", messagingController.messagesFromUser())
    }


}