package com.asapp.asMessagin.challenge.controller

import com.fasterxml.jackson.databind.ObjectWriter
import spark.Filter
import spark.Spark.*

class Routes(
    //private val logInController: LogInController,
    private val userController: UserController
    //private val messagingController: MessagingController
) {
    fun register() {
        // post("/user", userController.createUser())
        post("/users", userController.createUser())
    }

    /*
    fun handleUserAuthToken() = Filter { req, _ ->

        val authenticationToken =
            requireNotNull(req.headers("token")) { "'token' header is required" }
        logInController.authenticateUser(authenticationToken)


    } */

}