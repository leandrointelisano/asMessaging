package com.asapp.asMessagin.challenge.service

import com.asapp.asMessagin.challenge.model.UserMessagePost
import com.asapp.asMessagin.challenge.persistence.UserPersistence
import com.fasterxml.jackson.databind.ObjectMapper

class MessagingService (private val messagePersistence: UserPersistence, private val objectMapper: ObjectMapper) {
    fun sendMessage(message: UserMessagePost) =
        messagePersistence.persistMessage(message.sender, message.recipient, objectMapper.writeValueAsString(message.content))


}
