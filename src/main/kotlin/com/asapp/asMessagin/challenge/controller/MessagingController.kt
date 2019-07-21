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
    fun messagesFromUser() = { req: Request, _: Response ->

        val recipient: Int = requireNotNull(req.queryParams("recipient").toInt()) { "'recipient' param is required" }
        val start: Int = requireNotNull(req.queryParams("start").toInt()) { "'start' param is required" }
        val limit: Int? = req.queryMap().hasKey("limit").takeIf { it }?.let { req.queryParams("limit").toInt() }
        objectMapper.writeValueAsString(messagingService.messages(recipient, start, limit))


    }

    fun sendMessage() =
        { req: Request, _: Response ->
            try {
                objectMapper.readValue<UserMessagePost>(req.body(), UserMessagePost::class.java)
            } catch (e: Exception) {
                throw BadRequestException("Invalid request body while sending message")
            }?.let { message ->
                messagingService.sendMessage(message).let { objectMapper.writeValueAsString(it) }

            }


        }
}