package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.controller.UserController
import com.asapp.asMessagin.challenge.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class ControllerModule : AbstractModule() {

    override fun configure() {}

    @Provides
    @Singleton
    fun userController(
        userService: UserService,
        mapper: KotlinModule
        ): UserController = UserController(
        userService,
        ObjectMapper().registerModule(mapper)

    )

}