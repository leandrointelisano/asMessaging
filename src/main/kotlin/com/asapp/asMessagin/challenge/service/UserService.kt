package com.asapp.asMessagin.challenge.service

import com.asapp.asMessagin.challenge.model.User
import com.asapp.asMessagin.challenge.model.UserPostRequest
import com.asapp.asMessagin.challenge.persistence.Persistence

/**
 * @class UserService has the responsibility of creating users in the system
 */
class UserService(private val messagePersistence: Persistence) {
    fun create(user: UserPostRequest): User =
        User(messagePersistence.createUser(user.username, user.password))


}
