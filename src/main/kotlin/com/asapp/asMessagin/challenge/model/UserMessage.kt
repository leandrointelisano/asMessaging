package com.asapp.asMessagin.challenge.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserMessage(
    val sender: User? = null,
    val recipient: User? = null,
    val content: MessageContent? = null,
    val id: Int,
    val timestamp: String

)

data class UserMessagePost(
    val sender: Int,
    val recipient: Int,
    val content: MessageContent
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(
            value = TextContent::class,
            name = "text"
        ),
        JsonSubTypes.Type(
            value = ImageContent::class,
            name = "image"
        ),
        JsonSubTypes.Type(
            value = VideoContent::class,
            name = "video"
        )
    ]
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