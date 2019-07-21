package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.exception.APIException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER
import org.eclipse.jetty.http.HttpStatus
import spark.*
import spark.ExceptionHandler
import java.lang.IllegalArgumentException
import java.util.logging.Level

class ExceptionHandler(private val objectMapper: ObjectMapper) {

    fun register() {
        exception(APIException::class.java, apiExceptionHandler())
        exception(IllegalArgumentException::class.java, illegalArgumentExceptionHandler())
        exception(InvalidFormatException::class.java, invalidFormatExceptionHandler())

    }

    private fun invalidFormatExceptionHandler(): ExceptionHandler<in InvalidFormatException> =
        ExceptionHandler { e: InvalidFormatException, req: Request, res: Response ->
            LOGGER.log(Level.SEVERE, "Exception occurred for REQUEST ${req.contextPath()}", e)

            val apiError = ApiError(
                HttpStatus.BAD_REQUEST_400,
                e.message ?: ""
            )

            res.body(apiError.toString(objectMapper))
            res.status(apiError.code)
            res.type(APPLICATION_JSON)
        }


    private fun illegalArgumentExceptionHandler() =
        ExceptionHandler { e: IllegalArgumentException, req: Request, res: Response ->
            LOGGER.log(Level.SEVERE, "Exception occurred for REQUEST ${req.contextPath()}", e)

            val apiError = ApiError(
                HttpStatus.BAD_REQUEST_400,
                e.message ?: ""
            )

            res.body(apiError.toString(objectMapper))
            res.status(apiError.code)
            res.type(APPLICATION_JSON)
        }

    private fun apiExceptionHandler() =
        ExceptionHandler { e: APIException, req: Request, res: Response ->
            LOGGER.log(Level.SEVERE, "Exception occurred for REQUEST ${req.contextPath()}", e)
            val apiError = ApiError(
                HttpStatus.BAD_REQUEST_400,
                e.message

            )

            res.body(apiError.toString(objectMapper))
            res.status(apiError.code)
            res.type(APPLICATION_JSON)
        }

    @Synchronized
    private fun <T : Exception> exception(exceptionClass: Class<T>, handler: ExceptionHandler<in T>) {
        ExceptionMapper.getServletInstance().map(
            exceptionClass,
            object : ExceptionHandlerImpl<T>(exceptionClass) {
                override fun handle(exception: T, request: Request, response: Response) {
                    handler.handle(exception, request, response)
                }
            }
        )
    }


    data class ApiError(
        val code: Int,
        val message: String
    ) {

        fun toString(mapper: ObjectMapper): String = mapper.writeValueAsString(this)
    }

    companion object {
        private const val APPLICATION_JSON = "application/json"
    }
}