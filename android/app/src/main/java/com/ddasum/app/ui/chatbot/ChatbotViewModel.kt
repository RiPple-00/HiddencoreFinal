package com.ddasum.app.ui.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddasum.app.data.model.ChatMessage
import com.ddasum.app.data.model.ChatRole
import com.ddasum.app.data.repository.ChatbotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotRepository: ChatbotRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState: StateFlow<ChatbotUiState> = _uiState.asStateFlow()

    fun onInputChange(value: String) {
        _uiState.update { it.copy(inputText = value) }
    }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isEmpty() || _uiState.value.isSending) return

        val history = _uiState.value.messages + ChatMessage(ChatRole.USER, text)
        _uiState.update { it.copy(messages = history, inputText = "", isSending = true, error = null) }

        viewModelScope.launch {
            runCatching { chatbotRepository.sendMessage(history) }
                .onSuccess { reply ->
                    _uiState.update {
                        it.copy(
                            messages = it.messages + ChatMessage(ChatRole.ASSISTANT, reply.message),
                            isSending = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSending = false, error = e.message ?: "챗봇 응답을 받지 못했습니다.") }
                }
        }
    }
}
