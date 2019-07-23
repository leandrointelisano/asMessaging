package com.asapp.asMessaging.challenge.injection

import com.asapp.asMessaging.challenge.persistence.Persistence
import com.asapp.asMessaging.challenge.service.AuthenticationService
import com.asapp.asMessaging.challenge.service.MessagingService
import com.asapp.asMessaging.challenge.service.UserService
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

/**
 * Spark class used for injection of dependencies
 */
class ServiceModule : AbstractModule() {
    override fun configure() {}


    @Provides
    @Singleton
    fun userService(
        persistence: Persistence
    ): UserService = UserService(
        persistence
    )

    @Provides
    @Singleton
    fun logInService(
        persistence: Persistence
    ): AuthenticationService = AuthenticationService(
        persistence
    )

    @Provides
    @Singleton
    fun messagingService(
        persistence: Persistence
    ): MessagingService = MessagingService(
        persistence
    )
}