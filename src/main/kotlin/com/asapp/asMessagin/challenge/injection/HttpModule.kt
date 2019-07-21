package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.controller.*
import com.asapp.asMessagin.challenge.service.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class HttpModule : AbstractModule() {

    override fun configure() {}

    @Provides
    @Singleton
    fun routesRegister(
        userController: UserController,
        mapper: KotlinModule,
        authenticationController: AuthenticationController,
        requestFilter: RequestFilter,
        messagingController: MessagingController
    ): Routes = Routes(
        authenticationController,
        userController,
        ObjectMapper().registerModule(mapper),
        requestFilter,
        messagingController
    )

    @Provides
    @Singleton
    fun exceptionHandler(
        mapper: KotlinModule
    ): ExceptionHandler = ExceptionHandler(
        ObjectMapper().registerModule(mapper)
    )

    @Provides
    @Singleton
    fun requestFilter(
        authenticationService: AuthenticationService,
        mapper: KotlinModule
    ): RequestFilter = RequestFilter(
        authenticationService,
        ObjectMapper().registerModule(mapper)
    )
}