package com.asapp.asMessaging.challenge.service

import com.asapp.asMessaging.challenge.model.User
import com.asapp.asMessaging.challenge.model.UserPostRequest
import com.asapp.asMessaging.challenge.persistence.Persistence

/**
 * @class UserService has the responsibility of creating users in the system
 */
class UserService(private val messagePersistence: Persistence) {
    fun create(user: UserPostRequest): User =
        User(messagePersistence.createUser(user.username, user.password))


}
