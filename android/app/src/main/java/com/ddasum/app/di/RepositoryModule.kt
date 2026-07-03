package com.ddasum.app.di

import com.ddasum.app.data.repository.AuthRepository
import com.ddasum.app.data.repository.AuthRepositoryImpl
import com.ddasum.app.data.repository.ChatbotRepository
import com.ddasum.app.data.repository.ChatbotRepositoryImpl
import com.ddasum.app.data.repository.GalleryRepository
import com.ddasum.app.data.repository.GalleryRepositoryImpl
import com.ddasum.app.data.repository.GuardianRepository
import com.ddasum.app.data.repository.GuardianRepositoryImpl
import com.ddasum.app.data.repository.MealRepository
import com.ddasum.app.data.repository.MealRepositoryImpl
import com.ddasum.app.data.repository.MedicationRepository
import com.ddasum.app.data.repository.MedicationRepositoryImpl
import com.ddasum.app.data.repository.PaymentRepository
import com.ddasum.app.data.repository.PaymentRepositoryImpl
import com.ddasum.app.data.repository.ReportRepository
import com.ddasum.app.data.repository.ReportRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// abstract class인 이유: @Binds 함수는 본문이 없어야 해서(그냥 매핑 선언), Dagger가
//   컴파일 시점에 "이 함수는 몸체를 채우지 마라"고 강제하는 게 abstract fun이기 때문.
//   (@Provides처럼 실제 로직이 필요하면 abstract가 아닌 object를 쓴다 — ApiModule/NetworkModule 참고)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // @Binds: "MealRepository 타입 요청이 오면 MealRepositoryImpl을 줘라"는 매핑표.
    //   MealRepositoryImpl에 이미 @Inject constructor가 있어서 Dagger가 만드는 법을 알고 있으므로,
    //   여기선 인터페이스 ↔ 구현체 연결만 해주면 됨 (본문 없이 파라미터를 그대로 반환하는 것처럼 선언).
    // @Singleton: MealRepositoryImpl도 앱 전체에서 1개만 만들어서 재사용.
    @Binds
    @Singleton
    abstract fun bindMealRepository(impl: MealRepositoryImpl): MealRepository

    // 아래부터는 전부 위와 동일한 패턴(@Binds + @Singleton) — 대상 Repository만 다름
    @Binds
    @Singleton
    abstract fun bindGalleryRepository(impl: GalleryRepositoryImpl): GalleryRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGuardianRepository(impl: GuardianRepositoryImpl): GuardianRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(impl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(impl: ReportRepositoryImpl): ReportRepository

    @Binds
    @Singleton
    abstract fun bindChatbotRepository(impl: ChatbotRepositoryImpl): ChatbotRepository

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(impl: MedicationRepositoryImpl): MedicationRepository
}
