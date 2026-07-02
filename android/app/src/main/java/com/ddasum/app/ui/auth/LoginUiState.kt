package com.ddasum.app.ui.auth

data class LoginUiState(
    val loginId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)
