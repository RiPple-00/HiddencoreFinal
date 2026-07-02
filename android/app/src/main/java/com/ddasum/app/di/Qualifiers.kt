package com.ddasum.app.di

import javax.inject.Qualifier

/** Retrofit instance pointed at the chatbot FastAPI service (separate host/port from the Spring API). */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatbotRetrofit
