package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.controller.Routes
import com.asapp.asMessagin.challenge.controller.UserController
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class HttpModule : AbstractModule() {

    override fun configure() {}

    @Provides
    @Singleton
    fun routesRegister(
        userController: UserController
    ): Routes = Routes(
        userController
    )
}