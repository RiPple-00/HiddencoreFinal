package com.ddasum.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddasum.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onLoginIdChange(value: String) {
        _uiState.update { it.copy(loginId = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun login() {
        val state = _uiState.value
        // 1단계 판단: 클라이언트 유효성 검사 — 여기서 걸리면 네트워크 요청 자체를 안 보냄
        if (state.loginId.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "아이디와 비밀번호를 입력해주세요.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // 2단계 판단: 서버 응답 성공/실패 분기 (authRepository.guardianLogin 내부에서 토큰 디코딩+저장까지 끝남)
            runCatching { authRepository.guardianLogin(state.loginId, state.password) }
                // loginSuccess=true는 "네비게이션하라"는 신호일 뿐, 실제 이동은 여기서 안 함.
                // AuthViewModel.isLoggedIn도 이 시점 뒤늦게 true로 바뀌지만, 이미 그려진 NavHost는
                // startDestination을 다시 안 보므로, LoginScreen이 이 플래그를 보고 직접 navigate해야 함.
                .onSuccess { _uiState.update { it.copy(isLoading = false, loginSuccess = true) } }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "로그인에 실패했습니다.") }
                }
        }
    }
}
