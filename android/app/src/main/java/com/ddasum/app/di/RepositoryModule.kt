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

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMealRepository(impl: MealRepositoryImpl): MealRepository

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
