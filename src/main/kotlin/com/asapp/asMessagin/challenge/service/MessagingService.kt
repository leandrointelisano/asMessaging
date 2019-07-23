package com.asapp.asMessagin.challenge.service

import com.asapp.asMessagin.challenge.model.UserMessage
import com.asapp.asMessagin.challenge.model.UserMessagePost
import com.asapp.asMessagin.challenge.persistence.Persistence
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * @class MessagingService that has the responsibility of sending and receiving all the messages related to a particular user
 */
class MessagingService(private val messagePersistence: Persistence) {
    fun sendMessage(message: UserMessagePost) = runBlocking {
        messagePersistence.persistMessage(
            message.sender,
            message.recipient,
            message.content
        )
    }

    fun messages(recipient: Int, start: Int, limit: Int?): List<UserMessage> = runBlocking {
        async { messagePersistence.userMessages(recipient, start, limit) }.await()
    }


}
