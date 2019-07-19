package com.asapp.asMessagin.challenge.model

data class UserMessage(
    val sender: User,
    val recipient: User,
    val content: MessageContent,
    val id: Int
)

interface MessageContent

interface MediaContent : MessageContent {
    val height: Int
    val width: Int
    val url: Int
}

data class TextContent(val text: String) : MessageContent

data class ImageContent(override val height: Int, override val width: Int, override val url: Int) : MediaContent

data class VideoContent(override val height: Int, override val width: Int, override val url: Int) : MediaContent