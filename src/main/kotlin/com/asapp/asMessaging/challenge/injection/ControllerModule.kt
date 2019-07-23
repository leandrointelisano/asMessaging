package com.asapp.asMessaging.challenge.injection

import com.asapp.asMessaging.challenge.controller.AuthenticationController
import com.asapp.asMessaging.challenge.controller.MessagingController
import com.asapp.asMessaging.challenge.controller.UserController
import com.asapp.asMessaging.challenge.service.AuthenticationService
import com.asapp.asMessaging.challenge.service.MessagingService
import com.asapp.asMessaging.challenge.service.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

/**
 * Spark class used for injection of dependencies
 */
class ControllerModule : AbstractModule() {

    override fun configure() {}

    @Provides
    @Singleton
    fun userController(
        userService: UserService
    ): UserController = UserController(
        userService,
        jacksonObjectMapper()

    )

    @Provides
    @Singleton
    fun authenticationController(
        authenticationService: AuthenticationService
    ): AuthenticationController = AuthenticationController(
        authenticationService,
        jacksonObjectMapper()
    )

    @Provides
    @Singleton
    fun messagingController(
        messagingService: MessagingService
    ): MessagingController = MessagingController(
        messagingService,
        jacksonObjectMapper()
    )

}