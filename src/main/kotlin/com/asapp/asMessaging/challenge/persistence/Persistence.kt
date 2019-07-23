package com.asapp.asMessaging.challenge.persistence

import com.asapp.asMessaging.challenge.exception.APIException
import com.asapp.asMessaging.challenge.exception.UserLogException
import com.asapp.asMessaging.challenge.model.MessageContent
import com.asapp.asMessaging.challenge.model.UserMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.eclipse.jetty.http.HttpStatus
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.Instant

/**
 * Persistence class that handles all the request from the services. It persists all the user data.
 */
class Persistence(private val objectMapper: ObjectMapper) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    fun persistMessage(messageSender: Int, messageRecipient: Int, messageContent: MessageContent) =
        transaction {
            LOGGER.info("Persisting message from userid $messageSender to userid $messageRecipient")
            val senderUser: User = user(messageSender)
            val recipientUser: User = user(messageRecipient)
            Message.new {
                sender = senderUser
                recipient = recipientUser
                content = objectMapper.writeValueAsString(messageContent)
                timestamp = Instant.now().toString()
                messageNumber = countUserMessages(messageRecipient) + 1
                LOGGER.info("Message persisted successfully")

            }
                .let {
                    UserMessage(
                        id = it.id.value,
                        timestamp = it.timestamp
                    )
                }
        }


    fun userMessages(userId: Int, from: Int, to: Int?): List<UserMessage> =
        transaction {
            LOGGER.info("Fetching all messages from userid $userId with limits(from $from to ${to?: "MAX"})")
                Message.find { Messages.recipient eq userId }.limitMessages(from, to).map { persistedMessage ->
                UserMessage(
                    id = persistedMessage.messageNumber,
                    sender = persistedMessage.sender.id.value,
                    recipient = persistedMessage.recipient.id.value,
                    content = objectMapper.readValue(persistedMessage.content, MessageContent::class.java),
                    timestamp = persistedMessage.timestamp
                )
            }
        }

    fun healthy(): Boolean =
        transaction {
            exec("SELECT 1")
        }.let { true }

    fun createUser(username: String, password: String): Int =
        transaction {
            LOGGER.info("Creating user $username")
                user(username, password)?.let { user -> (user.username == username).takeIf { it }?.let { user.id.value } }
                ?: User.new {
                    this.username = username
                    this.password = DigestUtils.shaHex(password)

                }.id.value


        }

    private fun user(userId: Int) =
        transaction { User.find { Users.id eq userId } }.firstOrNull()?.let { it } ?: throw throw APIException(
            HttpStatus.NOT_FOUND_404,
            "User not found for user ID $userId"
        )

    private fun user(username: String, password: String) =
        User.find {
            (Users.username eq username) and
                    (Users.password eq DigestUtils.shaHex(password))
        }.firstOrNull()

    private fun alreadyLoggedUser(userId: Int): LoggedUser? =
        LoggedUser.find {
            UserLogin.userId eq userId
        }.firstOrNull()


    fun logUser(username: String, password: String): AuthenticatedUser =
        transaction {
            LOGGER.info("Logging in user $username")
            user(username, password)?.let { user ->
                alreadyLoggedUser(user.id.value)?.let { it } ?: transaction {
                    LoggedUser.new {
                        this.userId = user
                        this.token = RandomStringUtils.random(10, true, true)
                        LOGGER.info("$username logged in successfully")

                    }
                }
            }?.let {
                AuthenticatedUser(
                    userId = it.userId.id.value,
                    token = it.token

                )
            }
                ?: throw UserLogException("Failed to authenticate user: $username. \n Please try with another credentials.")
        }

    fun logoutUser(username: String, password: String) {
        transaction {
            user(username, password)?.let { loggedUser ->
                LoggedUser.find { UserLogin.userId eq loggedUser.id }.firstOrNull()?.let { LoggedUser[it.id].delete() }
                LOGGER.info("$username logged out successfully")

            }
        }

    }

    /*
    4 Bytes long for address 2,147,483,647 messages (Java Integer range is lower than SQL Lite Integer that uses up to 8 Bytes)
     */
    private fun countUserMessages(userId: Int): Int =
        transaction {
            Message.find { Messages.recipient eq userId }.count()
        }


    fun validToken(token: String, userId: Int): Boolean =
        transaction {
            LoggedUser.find { (UserLogin.token eq token) }
                .firstOrNull()
                ?.validateTokenOwner(userId)
                ?.let { it } ?: false

        }

    object Users : IntIdTable() {
        val username = varchar("username", 50).uniqueIndex()
        val password = varchar("password", length = 100)
    }


    object Messages : IntIdTable() {
        val recipient = reference("recipient", Users)
        val sender = reference("sender", Users)
        val content = varchar("content", length = 200)
        val timestamp = varchar("timestamp", length = 30)
        val messageNumber = integer("message_number")
    }

    object UserLogin : IntIdTable() {
        val userId = reference("user", Users)
        val token = varchar("token", length = 50).uniqueIndex()
    }

    class User(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<User>(Users)

        var username by Users.username
        var password by Users.password
    }

    class Message(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<Message>(Messages)

        var recipient by User referencedOn Messages.recipient
        var sender by User referencedOn Messages.sender
        var content by Messages.content
        var timestamp by Messages.timestamp
        var messageNumber by Messages.messageNumber
    }

    class LoggedUser(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<LoggedUser>(UserLogin)

        var userId by User referencedOn UserLogin.userId
        var token by UserLogin.token
    }

    data class AuthenticatedUser(
        val userId: Int,
        val token: String
    )
}

private fun SizedIterable<Persistence.Message>.limitMessages(from: Int, to: Int?): List<Persistence.Message> =
    this.takeIf { !it.empty() }
        .let { messages -> to?.let { messages?.filter { message -> message.messageNumber in from..to } } }
        ?: this.filter { it.messageNumber >= from }.toList()


private fun Persistence.LoggedUser?.validateTokenOwner(userId: Int): Boolean =
    this?.userId?.id?.value?.equals(userId) ?: false


