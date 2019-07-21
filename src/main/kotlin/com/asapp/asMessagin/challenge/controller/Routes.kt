package com.asapp.asMessagin.challenge.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import spark.Filter
import spark.Spark.*

class Routes(
    private val logInController: LogInController,
    private val userController: UserController,
    private val objectMapper: ObjectMapper
    //private val messagingController: MessagingController
) {
    fun register() {
        post("/users", userController.createUser())
        post("/login", logInController.login())
        post("/logout", logInController.logout())
    }

    /*
    fun handleUserAuthToken() = Filter { req, _ ->

        val authenticationToken =
            requireNotNull(req.headers("token")) { "'token' header is required" }
        logInController.authenticateUser(authenticationToken)


    } */

}