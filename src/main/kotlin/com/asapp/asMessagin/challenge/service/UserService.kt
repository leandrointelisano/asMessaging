package com.asapp.asMessagin.challenge.service

import com.asapp.asMessagin.challenge.model.User
import com.asapp.asMessagin.challenge.model.UserPostRequest
import com.asapp.asMessagin.challenge.persistence.UserPersistence


class UserService(private val messagePersistence: UserPersistence) {
    fun create(user: UserPostRequest): User =
        User(messagePersistence.createUser(user.username, user.password))


}
