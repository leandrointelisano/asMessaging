package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.persistence.UserPersistence
import com.asapp.asMessagin.challenge.service.LogInService
import com.asapp.asMessagin.challenge.service.UserService
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class ServiceModule : AbstractModule() {
    override fun configure() {}


    @Provides
    @Singleton
    fun userService(
        persistence: UserPersistence
    ): UserService = UserService(
        persistence
    )

    @Provides
    @Singleton
    fun logInService(
        persistence: UserPersistence
    ): LogInService = LogInService(
        persistence
    )
}