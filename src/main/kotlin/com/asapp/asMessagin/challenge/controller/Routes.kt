package com.asapp.asMessagin.challenge.controller

import spark.Spark.*

class Routes(
    private val authenticationController: AuthenticationController,
    private val userController: UserController,
    private val requestFilter: RequestFilter,
    private val messagingController: MessagingController,
    private val appController: AppController
) {
    fun register() {
        post("/check", appController.healthCheck())
        post("/users", userController.createUser())
        post("/login", authenticationController.login())
        post("/logout", authenticationController.logout())
        before("/messages", requestFilter.handleToken())
        post("/messages", messagingController.sendMessage())
        get("/messages", messagingController.messagesFromUser())
    }


}