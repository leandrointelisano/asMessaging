package com.asapp.asMessagin.challenge.exception

import org.eclipse.jetty.http.HttpStatus

/**
 * @class UserLogException thrown each time that the system is not able to perform the login of an existing user
 */
class UserLogException(
    message: String,
    cause: Throwable? = null
) : APIException(HttpStatus.CONFLICT_409, message, cause)

/**
 * @class UnauthorizedException thrown each time that the provided token does not fit the login records of the system
 */
class UnauthorizedException(
    message: String,
    cause: Throwable? = null
) : APIException(HttpStatus.UNAUTHORIZED_401, message, cause)