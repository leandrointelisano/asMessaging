package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.persistence.MessagePersistence
import com.asapp.asMessagin.challenge.service.UserService
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class ServiceModule : AbstractModule() {
    override fun configure() {}


    @Provides
    @Singleton
    fun userService(
        persistence: MessagePersistence
    ): UserService = UserService(
        persistence
    )
}