package com.ddasum.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddasum.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {

    // authRepository.isLoggedIn은 Flow<Boolean>(null 없음, true/false뿐).
    // stateIn()은 StateFlow가 "항상 즉시 읽을 수 있는 현재값"을 가져야 해서 초기값이 필수인데,
    // DataStore 첫 읽기(비동기, 디스크 I/O)가 도착하기 전엔 true/false 둘 중 뭘 넣어도 거짓말이 된다.
    // 그래서 여기서만 null을 인위적으로 만들어서 "아직 판단 전"이라는 3번째 상태로 씀
    // (Repository/DataStore 계층엔 이 null 개념 자체가 없음 — AuthRepositoryImpl.isLoggedIn 참고).
    val isLoggedIn: StateFlow<Boolean?> = authRepository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
