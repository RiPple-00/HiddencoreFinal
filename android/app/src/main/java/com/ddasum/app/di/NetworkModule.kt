package com.ddasum.app.di

import com.ddasum.app.BuildConfig
import com.ddasum.app.data.remote.AuthInterceptor
import com.ddasum.app.data.remote.LongTimeoutInterceptor
import com.ddasum.app.data.remote.api.AuthApiService
import com.ddasum.app.data.remote.api.ChatbotApiService
import com.ddasum.app.data.remote.api.GalleryApiService
import com.ddasum.app.data.remote.api.GuardianApiService
import com.ddasum.app.data.remote.api.MealApiService
import com.ddasum.app.data.remote.api.MedicationApiService
import com.ddasum.app.data.remote.api.PaymentApiService
import com.ddasum.app.data.remote.api.ReportApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        longTimeoutInterceptor: LongTimeoutInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(longTimeoutInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @ChatbotRetrofit
    fun provideChatbotRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.CHATBOT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMealApiService(retrofit: Retrofit): MealApiService =
        retrofit.create(MealApiService::class.java)

    @Provides
    @Singleton
    fun provideGalleryApiService(retrofit: Retrofit): GalleryApiService =
        retrofit.create(GalleryApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideGuardianApiService(retrofit: Retrofit): GuardianApiService =
        retrofit.create(GuardianApiService::class.java)

    @Provides
    @Singleton
    fun providePaymentApiService(retrofit: Retrofit): PaymentApiService =
        retrofit.create(PaymentApiService::class.java)

    @Provides
    @Singleton
    fun provideReportApiService(retrofit: Retrofit): ReportApiService =
        retrofit.create(ReportApiService::class.java)

    @Provides
    @Singleton
    fun provideMedicationApiService(retrofit: Retrofit): MedicationApiService =
        retrofit.create(MedicationApiService::class.java)

    @Provides
    @Singleton
    fun provideChatbotApiService(@ChatbotRetrofit retrofit: Retrofit): ChatbotApiService =
        retrofit.create(ChatbotApiService::class.java)
}
