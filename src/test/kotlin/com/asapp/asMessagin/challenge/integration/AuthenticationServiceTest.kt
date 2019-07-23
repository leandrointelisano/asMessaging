package com.asapp.asMessagin.challenge.integration

import com.asapp.asMessagin.challenge.model.UserPostRequest
import com.asapp.asMessagin.challenge.persistence.Persistence
import com.asapp.asMessagin.challenge.service.AuthenticationService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.WordSpec

class AuthenticationServiceTest : WordSpec() {
    private val passwordUser1 = "pass1"
    private val passwordUser2 = "pass2"
    private val user1Id = 1
    private val user2Id = 2
    private val username1 = "TestUser1"
    private val username2 = "TestUser2"
    private val user1PostRequest = UserPostRequest(username1, passwordUser1)
    private val user2PostRequest = UserPostRequest(username2, passwordUser2)
    private val authenticationService = AuthenticationService(Persistence(jacksonObjectMapper()))


    init {
        initializeTestDatabase()

        "authenticate user1 should return with its id and token" should {
            val response = authenticationService.loginUser(user1PostRequest)
            response.id shouldBe user1Id
            response.token shouldNotBe response.token.isEmpty()
        }

        "authenticate user2 should return its id and token and must be a valid token" should {
            val response = authenticationService.loginUser((user2PostRequest))
            response.id shouldBe user2Id
            response.token shouldNotBe response.token.isEmpty()
            authenticationService.validToken(response.token, user2Id) shouldBe true

        }

        "logging out user2 must invalidate token" should {
            val tokenUser2= authenticationService.loginUser(user2PostRequest).token
            authenticationService.logoutUser(user2PostRequest)
            authenticationService.validToken(tokenUser2, user2Id) shouldBe false
        }

        "user2 token must not be a valid token with user1Id" should {
            val tokenUser2= authenticationService.loginUser(user2PostRequest).token
            authenticationService.logoutUser(user2PostRequest)
            authenticationService.validToken(tokenUser2, user1Id) shouldBe false
        }



    }
}