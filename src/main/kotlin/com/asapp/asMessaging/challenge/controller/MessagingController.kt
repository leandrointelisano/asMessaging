package com.asapp.asMessaging.challenge.controller

import com.asapp.asMessaging.challenge.exception.BadRequestException
import com.asapp.asMessaging.challenge.model.UserMessagePost
import com.asapp.asMessaging.challenge.service.MessagingService
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Response

/**
 * @class {MessagingController}
 * It handles all the messages request, sending and receiving messages from a particular user.
 * It connects the MessagingService with its route
 * @throws BadRequestException when request body does not fit the correct object or when a query parameter is missing
 */
class MessagingController(
    private val messagingService: MessagingService, private val objectMapper: ObjectMapper
) {
    fun messagesFromUser() = { req: Request, _: Response ->
        try {
            val recipient: Int =
                requireNotNull(req.queryParams("recipient").toInt()) { "'recipient' param is required" }
            val start: Int = requireNotNull(req.queryParams("start").toInt()) { "'start' param is required" }
            val limit: Int? = req.queryMap().hasKey("limit").takeIf { it }?.let { req.queryParams("limit").toInt() }
            objectMapper.writeValueAsString(messagingService.messages(recipient, start, limit))
        } catch (e: Exception) {
            throw BadRequestException("Missing or invalid parameter: ${e.message}", e)
        }


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