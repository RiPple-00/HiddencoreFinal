package com.ddasum.app.di

import javax.inject.Qualifier

/** 챗봇 FastAPI 서비스를 가리키는 Retrofit 인스턴스용 이름표 (Spring API와 host/port가 다름). */
// 어노테이션 3개의 의미는 DispatcherQualifiers.kt 상단 주석과 동일 (이름표 붙이는 방법은 항상 같음).
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatbotRetrofit
