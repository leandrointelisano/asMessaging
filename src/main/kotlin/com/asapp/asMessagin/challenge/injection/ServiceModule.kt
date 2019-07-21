package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.persistence.UserPersistence
import com.asapp.asMessagin.challenge.service.AuthenticationService
import com.asapp.asMessagin.challenge.service.MessagingService
import com.asapp.asMessagin.challenge.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
    ): AuthenticationService = AuthenticationService(
        persistence
    )

    @Provides
    @Singleton
    fun messagingService(
        persistence: UserPersistence,
        mapper: KotlinModule
    ): MessagingService = MessagingService(
        persistence,
        ObjectMapper().registerModule(mapper)
    )
}