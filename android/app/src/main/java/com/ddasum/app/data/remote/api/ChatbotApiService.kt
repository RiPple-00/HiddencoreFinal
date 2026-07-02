package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.chatbot.ChatRequestDto
import com.ddasum.app.data.remote.dto.chatbot.ChatResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/** Calls the standalone chatbot FastAPI service directly — not proxied through the Spring backend. */
interface ChatbotApiService {

    @POST("chat")
    suspend fun sendMessage(@Body request: ChatRequestDto): ChatResponseDto
}
