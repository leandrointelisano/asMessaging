package com.asapp.asMessagin.challenge.exception

import org.eclipse.jetty.http.HttpStatus

class AlreadyLoggedException(
    message: String,
    cause: Throwable? = null
) : APIException(HttpStatus.CONFLICT_409, message, cause)

class UserNotLoggedException(
    message: String,
    cause: Throwable? = null
) : APIException(HttpStatus.CONFLICT_409, message, cause)