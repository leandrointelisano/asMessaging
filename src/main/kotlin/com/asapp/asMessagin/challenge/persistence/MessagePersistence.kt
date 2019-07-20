package com.asapp.asMessagin.challenge.persistence

import com.asapp.asMessagin.challenge.model.UserMessage
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class MessagePersistence {

    fun persistMessage(userMessage: UserMessage) {
        TODO()
    }

    fun userMessages(userId: Int, from: Int, to: Int): List<UserMessage> {
        TODO()
    }

    fun createUser(username: String, password: String): Int =
        transaction {
            User.new {
                this.username = username
                this.password = password

            }.id.value
        }


    fun user(userId: Int) = User[userId]

    object Users : IntIdTable() {
        val username = varchar("username", 50)
        val password = varchar("password", length = 16)
    }


    object Messages : IntIdTable() {
        val recipient = reference("recipient", Users)
        val sender = reference("sender", Users)
        val content = varchar("content", length = 200)
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
    }

    class LoggedUser(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<LoggedUser>(UserLogin)

        var userId by User referencedOn UserLogin.userId
        var token by UserLogin.token
    }


    fun dbInit() {

        Database.connect("jdbc:sqlite:/resources/data.db", "org.sqlite.JDBC")

        transaction { SchemaUtils.create(Users, Messages, UserLogin) }


    }

}

