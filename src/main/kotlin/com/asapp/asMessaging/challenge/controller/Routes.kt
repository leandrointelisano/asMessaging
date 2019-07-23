package com.asapp.asMessaging.challenge.controller

import spark.Spark.*

/**
 * @class @Routes registers all available endpoints and wire them with each controller.
 * Also registers the token Filter before each Message endpoint operation
 */
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

        before("messages\b",requestFilter.handleTokenAndSenderUserId())
        post("/messages", messagingController.sendMessage())

        before("/messages?", requestFilter.handleValidToken())
        get("/messages", messagingController.messagesFromUser())


    }


}