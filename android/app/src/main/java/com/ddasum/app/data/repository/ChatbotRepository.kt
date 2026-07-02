package com.ddasum.app.data.repository

import com.ddasum.app.data.model.ChatMessage
import com.ddasum.app.data.model.ChatReply

interface ChatbotRepository {
    suspend fun sendMessage(history: List<ChatMessage>, language: String = "ko"): ChatReply
}
