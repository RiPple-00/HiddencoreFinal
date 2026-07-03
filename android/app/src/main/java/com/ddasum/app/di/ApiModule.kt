package com.ddasum.app.di

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
import retrofit2.Retrofit
import javax.inject.Singleton

/** 어떤 엔드포인트 인터페이스가 있는지 정의 — NetworkModule이 제공하는 Retrofit 인스턴스로 만든다. */
@Module                                        // "이 안에 provide/binds 레시피가 들어있다" 표시
@InstallIn(SingletonComponent::class)          // 앱이 살아있는 동안 이 모듈의 결과물을 계속 재사용
object ApiModule {

    // @Provides: retrofit.create(...)처럼 내가 직접 코드를 실행해서 만들어야 하는 것
    // @Singleton: 앱 전체에서 이 함수는 딱 한 번만 실행되고, 이후엔 결과가 캐시되어 재사용됨
    //   (retrofit.create()는 리플렉션으로 동적 프록시를 만드는 비용이 있어서 매번 새로 만들면 손해)
    @Provides
    @Singleton
    fun provideMealApiService(retrofit: Retrofit): MealApiService =
        retrofit.create(MealApiService::class.java)

    // 아래부터는 전부 위와 동일한 패턴(@Provides + @Singleton, retrofit.create) — 대상 인터페이스만 다름
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

    // 이 함수만 파라미터에 @ChatbotRetrofit 이름표가 붙음 — Retrofit이 2개(기본/챗봇용)라
    // Hilt에게 "어떤 Retrofit을 달라는 건지" 구분해줘야 하기 때문 (NetworkModule 3-6 참고)
    @Provides
    @Singleton
    fun provideChatbotApiService(@ChatbotRetrofit retrofit: Retrofit): ChatbotApiService =
        retrofit.create(ChatbotApiService::class.java)
}
