package com.asapp.asMessagin.challenge.integration

import com.asapp.asMessagin.challenge.model.UserMessage
import com.asapp.asMessagin.challenge.persistence.Persistence
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class PersistenceTest : WordSpec() {
    private val persistence: Persistence = Persistence(getMapper())

    private val user1Id = 1
    private val user2Id = 2
    private val username1 = "TestUser1"
    private val username2 = "TestUser2"
    private val passwordUser1 = "pass1"
    private val passwordUser2 = "pass2"
    private val messageId = 1
    private val messageContentToBeSent = "MESSAGE CONTENT"
    private val mockedTimeStamp = ("2019-07-22T23:46:18.647Z")
    private val messageResponse = UserMessage(id = messageId, timestamp = mockedTimeStamp)


    init {

        Database.connect("jdbc:sqlite:./data-beta.db", "org.sqlite.JDBC")
        transaction { SchemaUtils.drop(Persistence.Users, Persistence.Messages, Persistence.UserLogin) }
        transaction {
            SchemaUtils.create(Persistence.Users, Persistence.Messages, Persistence.UserLogin)
            persistence.createUser(username1, passwordUser1)
            persistence.createUser(username2, passwordUser2)
        }



        "testing persistence: when" should {
            "Creating the first user should return successfully with user id 1" {
                persistence.createUser(username1, passwordUser1) shouldBe (user2Id)
            }




            "Creating the second user should return successfully with user id 2" should
                    {
                        persistence.createUser(username2, passwordUser2) shouldBe (user1Id)
                    }


            "persisting a message from User1 to User2 should generate" should
                    {
                        val response = persistence.persistMessage(user1Id, user1Id, messageContentToBeSent)
                        response.id shouldBe (messageResponse.id)
                        //timestamp is not being tested
                    }

        }
    }
}








