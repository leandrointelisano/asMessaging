package com.asapp.asMessagin.challenge.exception

open class APIException(
    val code: Int,
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)