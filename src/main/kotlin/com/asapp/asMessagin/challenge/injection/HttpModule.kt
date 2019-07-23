package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.controller.*
import com.asapp.asMessagin.challenge.persistence.Persistence
import com.asapp.asMessagin.challenge.service.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

/**
 * Spark class used for injection of dependencies
 */
class HttpModule : AbstractModule() {

    override fun configure() {}

    @Provides
    @Singleton
    fun routesRegister(
        userController: UserController,
        authenticationController: AuthenticationController,
        requestFilter: RequestFilter,
        messagingController: MessagingController,
        appController: AppController
    ): Routes = Routes(
        authenticationController,
        userController,
        requestFilter,
        messagingController,
        appController
    )

    @Provides
    @Singleton
    fun exceptionHandler(): ExceptionHandler = ExceptionHandler(jacksonObjectMapper())

    @Provides
    @Singleton
    fun requestFilter(
        authenticationService: AuthenticationService
    ): RequestFilter = RequestFilter(
        authenticationService,
        jacksonObjectMapper()
    )

    @Provides
    @Singleton
    fun appController(
        persistence: Persistence
    ): AppController = AppController(
        persistence,
        jacksonObjectMapper()
    )
}