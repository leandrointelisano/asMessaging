package com.asapp.asMessaging.challenge.model

/**
 * User data models
 */
data class UserPostRequest (
    val username: String,
    val password: String
)

data class User (
    val id: Int
)

data class LoggedUser(
    val id: Int,
    val token: String
)
