package com.ddasum.app.data.remote.dto.chatbot

/** Mirrors ai/chatbot/chatbot.py Pydantic models (ChatRequest/ChatResponse). */
data class ChatMessageDto(val role: String, val content: String)

data class ChatRequestDto(val messages: List<ChatMessageDto>, val language: String = "ko")

data class ChatResponseDto(val reply: String, val sources: List<String> = emptyList())
