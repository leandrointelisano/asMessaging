package com.asapp.asMessagin.challenge.exception

import org.eclipse.jetty.http.HttpStatus

/**
 * @class BadRequestException thrown each time that a request body could not be parsed
 */
class BadRequestException(message: String,
                          cause: Throwable? = null) : APIException(HttpStatus.BAD_REQUEST_400, message, cause)
