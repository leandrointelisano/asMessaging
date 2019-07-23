package com.asapp.asMessagin.challenge.integration

import com.asapp.asMessagin.challenge.persistence.Persistence
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

inline fun <reified T> getMock() = Mockito.mock(T::class.java)
fun <A> on(methodCall: A): OngoingStubbing<A> = Mockito.`when`(methodCall)

fun getMapper() = jacksonObjectMapper()

fun initializeTestDatabase(): Persistence {
    val username1 = "TestUser1"
    val username2 = "TestUser2"
    val passwordUser1 = "pass1"
    val passwordUser2 = "pass2"
    val persistence: Persistence = Persistence(getMapper())

    Database.connect("jdbc:sqlite:./data-beta.db", "org.sqlite.JDBC")
    transaction { SchemaUtils.drop(Persistence.Users, Persistence.Messages, Persistence.UserLogin) }
    transaction {
        SchemaUtils.create(Persistence.Users, Persistence.Messages, Persistence.UserLogin)
        persistence.createUser(username1, passwordUser1)
        persistence.createUser(username2, passwordUser2)
    }
    return persistence
}

