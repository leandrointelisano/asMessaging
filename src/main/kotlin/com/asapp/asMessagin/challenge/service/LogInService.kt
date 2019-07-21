package com.asapp.asMessagin.challenge.service

import com.asapp.asMessagin.challenge.model.LoggedUser
import com.asapp.asMessagin.challenge.model.UserPostRequest
import com.asapp.asMessagin.challenge.persistence.UserPersistence

class LogInService(private val userPersistence: UserPersistence) {

    fun loginUser(user: UserPostRequest) =
        userPersistence.logUser(user.username, user.password)?.let { loggedUser ->
            LoggedUser(
                id = loggedUser.userId,
                token = loggedUser.token
            )
        }

    fun logoutUser(user: UserPostRequest) =
            userPersistence.logoutUser(user.username, user.password)


}
