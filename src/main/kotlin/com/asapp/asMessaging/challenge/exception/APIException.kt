package com.asapp.asMessaging.challenge.exception

/**
 * @Class APIException super class used for general purposes exceptions (wrapper)
 */
open class APIException(
    val code: Int,
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)