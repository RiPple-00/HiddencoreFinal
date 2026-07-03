package com.ddasum.app.di

import javax.inject.Qualifier

/** Retrofit instance pointed at the chatbot FastAPI service (separate host/port from the Spring API). */
// 어노테이션 3개의 의미는 DispatcherQualifiers.kt 상단 주석과 동일 (이름표 붙이는 방법은 항상 같음).
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatbotRetrofit
