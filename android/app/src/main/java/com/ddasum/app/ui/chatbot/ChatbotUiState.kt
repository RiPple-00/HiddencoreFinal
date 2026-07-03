package com.ddasum.app.ui.chatbot

import com.ddasum.app.data.model.ChatMessage

data class ChatbotUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false,
    val error: String? = null
)
