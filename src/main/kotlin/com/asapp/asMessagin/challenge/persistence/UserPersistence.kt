package com.asapp.asMessagin.challenge.persistence

import com.asapp.asMessagin.challenge.exception.UserNotLoggedException
import com.asapp.asMessagin.challenge.model.MessageContent
import com.asapp.asMessagin.challenge.model.UserMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.RandomStringUtils
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class UserPersistence(private val objectMapper: ObjectMapper) {

    fun persistMessage(messageSender: Int, messageRecipient: Int, messageContent: String) =
        transaction {
            val senderUser: User = user(messageSender)
            val recipientUser: User = user(messageRecipient)
            Message.new {
                sender = senderUser
                recipient = recipientUser
                content = messageContent
                timestamp = Instant.now().toString()
                messageNumber = countUserMessages(messageRecipient) + 1
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


    fun createUser(username: String, password: String): Int =
        transaction {
            User.new {
                this.username = username
                this.password = password

            }.id.value
        }

    private fun user(userId: Int) =
        transaction { User[userId] }

    private fun user(username: String, password: String) =
        User.find {
            (Users.username eq username) and
                    (Users.password eq password)
        }.firstOrNull()

    private fun alreadyLoggedUser(userId: Int): LoggedUser? =
        LoggedUser.find {
            UserLogin.userId eq userId
        }.firstOrNull()


    fun logUser(username: String, password: String): AuthenticatedUser? =
        transaction {
            user(username, password)?.let { user ->
                alreadyLoggedUser(user.id.value)?.let { it } ?: transaction {
                    LoggedUser.new {
                        this.userId = user
                        this.token = RandomStringUtils.random(10, true, true)
                    }
                }
            }?.let {
                AuthenticatedUser(
                    userId = it.userId.id.value,
                    token = it.token
                )
            }
                ?: throw UserNotLoggedException("Failed to authenticate user: $username. \n Please try with another credentials.")
        }

    fun logoutUser(username: String, password: String) {
        transaction {
            user(username, password)?.let { loggedUser ->
                LoggedUser.find { UserLogin.userId eq loggedUser.id }.firstOrNull()?.let { LoggedUser[it.id].delete() }
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

    object Users : IntIdTable() {
        val username = varchar("username", 50).uniqueIndex()
        val password = varchar("password", length = 16)
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
        val token = varchar("token", length = 50)
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


    fun dbInit() {

        Database.connect("jdbc:sqlite:/resources/data.db", "org.sqlite.JDBC")

        transaction { SchemaUtils.create(Users, Messages, UserLogin) }


    }

    fun validToken(token: String, userId: Int): Boolean =
        transaction {
            val user = user(userId)
            LoggedUser.find { (UserLogin.token eq token) }
                .firstOrNull()
                ?.validateSenderUser(userId)
                ?.let { it } ?: false

        }


}

private fun SizedIterable<UserPersistence.Message>.limitMessages(from: Int, to: Int?): List<UserPersistence.Message> =
    this.takeIf { !it.empty() }
        .let { messages -> to?.let { messages?.filter { message -> message.messageNumber in from..to } } }
        ?: this.filter { it.messageNumber >= from }.toList()


private fun UserPersistence.LoggedUser?.validateSenderUser(userId: Int): Boolean =
    this?.userId?.id?.value?.equals(userId) ?: false


