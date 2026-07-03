package com.ddasum.app.di

import com.ddasum.app.BuildConfig
import com.ddasum.app.data.remote.AuthInterceptor
import com.ddasum.app.data.remote.LongTimeoutInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * "서버와 어떻게 통신하는가"만 담당 — OkHttp/Retrofit/Interceptor.
 * 어떤 엔드포인트 인터페이스가 있는지는 별개 관심사라 ApiModule 참고.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // authInterceptor/longTimeoutInterceptor 파라미터는 Hilt가 자동으로 채워줌 —
    //   두 Interceptor 클래스에 @Inject constructor가 붙어있어서(data/remote/*.kt),
    //   이 함수는 "이미 만들어진 재료를 조립"만 하면 됨.
    // 연결 풀(TCP)을 재사용해야 하므로 @Singleton — 화면마다 새로 만들면 매번 재연결돼서 느려짐.
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

    // 이름표 없는 기본 Retrofit — Spring 백엔드(BuildConfig.BASE_URL, dev/prod로 값만 바뀜)용.
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // @ChatbotRetrofit: 바로 위와 같은 Retrofit 타입인데 baseUrl만 다름(챗봇 전용 8001 서비스).
    //   이름표가 없으면 Hilt가 "Retrofit을 달라"는 요청 2개를 구분 못 해서 컴파일 에러가 남.
    @Provides
    @Singleton
    @ChatbotRetrofit
    fun provideChatbotRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.CHATBOT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
