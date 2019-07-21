package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.controller.*
import com.asapp.asMessagin.challenge.persistence.UserPersistence
import com.asapp.asMessagin.challenge.service.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
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

    @Provides
    @Singleton
    fun appController(
        mapper: KotlinModule,
        userPersistence: UserPersistence
    ): AppController = AppController(
        userPersistence,
        ObjectMapper().registerModule(mapper)
    )
}