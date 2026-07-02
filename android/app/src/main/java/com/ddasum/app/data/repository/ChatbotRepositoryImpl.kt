package com.ddasum.app.data.repository

import com.ddasum.app.data.model.ChatMessage
import com.ddasum.app.data.model.ChatReply
import com.ddasum.app.data.model.ChatRole
import com.ddasum.app.data.remote.api.ChatbotApiService
import com.ddasum.app.data.remote.dto.chatbot.ChatMessageDto
import com.ddasum.app.data.remote.dto.chatbot.ChatRequestDto
import javax.inject.Inject

class ChatbotRepositoryImpl @Inject constructor(
    private val api: ChatbotApiService
) : ChatbotRepository {

    override suspend fun sendMessage(history: List<ChatMessage>, language: String): ChatReply {
        val request = ChatRequestDto(
            messages = history.map { ChatMessageDto(role = it.role.toWireValue(), content = it.content) },
            language = language
        )
        val response = api.sendMessage(request)
        return ChatReply(message = response.reply, sources = response.sources)
    }

    private fun ChatRole.toWireValue(): String = when (this) {
        ChatRole.USER -> "user"
        ChatRole.ASSISTANT -> "assistant"
    }
}
