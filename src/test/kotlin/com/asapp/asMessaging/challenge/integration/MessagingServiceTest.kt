package com.asapp.asMessaging.challenge.integration

import com.asapp.asMessaging.challenge.model.TextContent
import com.asapp.asMessaging.challenge.model.UserMessage
import com.asapp.asMessaging.challenge.model.UserMessagePost
import com.asapp.asMessaging.challenge.persistence.Persistence
import com.asapp.asMessaging.challenge.service.MessagingService
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec

class MessagingServiceTest : WordSpec() {

    private val persistence: Persistence = Persistence(getMapper())
    private val messagingService = MessagingService(persistence)

    private val user1Id = 1
    private val user2Id = 2
    private val username1 = "TestUser1"
    private val username2 = "TestUser2"
    private val passwordUser1 = "pass1"
    private val passwordUser2 = "pass2"
    private val messageId = 1
    private val message2Id = 2
    private val messageContentToBeSent = com.asapp.asMessaging.challenge.model.TextContent("Message content")
    private val mockedTimeStamp = ("2019-07-22T23:46:18.647Z")
    private val messageResponse = com.asapp.asMessaging.challenge.model.UserMessage(id = 1, timestamp = mockedTimeStamp)
    private val messageResponse2 =
        com.asapp.asMessaging.challenge.model.UserMessage(id = 2, timestamp = mockedTimeStamp)
    private val message1ToBeSentObject =
        com.asapp.asMessaging.challenge.model.UserMessagePost(user1Id, user2Id, messageContentToBeSent)



    init {

        initializeTestDatabase()

        "testing persistence: when" should {
            "Creating the first user should return successfully with user id 1" {
                persistence.createUser(username1, passwordUser1) shouldBe (user1Id)
            }

            "Creating the second user should return successfully with user id 2" should
                    {
                        persistence.createUser(username2, passwordUser2) shouldBe (user2Id)
                    }


            "persisting a message from User1 to User2 should generate the message response with messageId 1" should
                    {
                        val response = messagingService.sendMessage(message1ToBeSentObject)
                        response.id shouldBe (messageResponse.id)
                        //timestamp is not being tested due to the need of mock a final class like Java Instant (Mockito must be used) - todo
                    }

            "persisting another message from User1 to User2 should generate the message response with messageId 2" should {
                val response = messagingService.sendMessage(message1ToBeSentObject)
                response.id shouldBe (messageResponse2.id)
            }

            "getting user1 received messages from init with no limit value should have 2 messages" should {
                val response = messagingService.messages(user2Id, 1, null)
                response.size shouldBe (2)

            }

            "getting user1 received messages from message id 2 to end should have 1 message" should {
                val response = messagingService.messages(user2Id, 2, null)
                response.size shouldBe (1)
            }


            "getting user2 received messages should have 0 messages" should {
                val response = messagingService.messages(user1Id, 1, null)
                response.size shouldBe (0)
            }

            "getting user1 received messages from init with limit value 1 should have 1 message and id 1" should {
                val response = messagingService.messages(user2Id, 1, 1)
                response.size shouldBe (1)
                response[0].id shouldBe (messageId)

            }

            "getting user1 received messages from init value 1 with limit value 2 should have 1 message and id 2" should {
                val response = messagingService.messages(user2Id, 2, 2)
                response.size shouldBe (1)
                response[0].id shouldBe (message2Id)

            }
        }
    }
}








