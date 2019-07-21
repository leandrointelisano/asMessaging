package com.asapp.asMessagin.challenge.controller

import com.asapp.asMessagin.challenge.exception.BadRequestException
import com.asapp.asMessagin.challenge.model.UserMessagePost
import com.asapp.asMessagin.challenge.service.MessagingService
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Response

class MessagingController(
    private val messagingService: MessagingService, private val objectMapper: ObjectMapper
) {
    fun messagesFromUser() = { req: Request, response: Response ->
        {
            val recipient: String = requireNotNull(req.queryParams("recipient")) { "'recipient' param is required" }
            val locale: String = requireNotNull(req.queryParams("recipient")) { "'recipient' param is required" }

        }
    }

    fun sendMessage() =
        { req: Request, response: Response ->
            try {
                objectMapper.readValue<UserMessagePost>(req.body(), UserMessagePost::class.java)

            } catch (e: Exception) {
                throw BadRequestException("Invalid request body while sending message")
            }?.let { message ->
                messagingService.sendMessage(message).let { objectMapper.writeValueAsString(it) }

            }


        }
}