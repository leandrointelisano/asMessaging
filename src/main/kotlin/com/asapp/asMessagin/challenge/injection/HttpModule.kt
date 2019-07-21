package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.controller.ExceptionHandler
import com.asapp.asMessagin.challenge.controller.LogInController
import com.asapp.asMessagin.challenge.controller.Routes
import com.asapp.asMessagin.challenge.controller.UserController
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
        logInController: LogInController
    ): Routes = Routes(
        logInController,
        userController,
        ObjectMapper().registerModule(mapper)

    )

    @Provides
    @Singleton
    fun exceptionHandler(
        mapper: KotlinModule
    ): ExceptionHandler = ExceptionHandler(
        ObjectMapper().registerModule(mapper)
    )
}