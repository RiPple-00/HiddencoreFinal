package com.ddasum.app.data.model

enum class ChatRole { USER, ASSISTANT }

data class ChatMessage(val role: ChatRole, val content: String)

data class ChatReply(val message: String, val sources: List<String>)
