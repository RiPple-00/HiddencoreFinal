package com.ddasum.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Repository가 Dispatchers.IO를 직접 호출하지 않고 주입받는 이유는, 테스트에서
 * TestDispatcher(가상 시간, 딜레이 없음)로 바꿔치기할 수 있게 하기 위해서다 —
 * Dispatchers.IO를 코드에 하드코딩하면 테스트가 불가능해진다.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    // @Provides: Dispatchers.IO 자체는 코틀린 표준 라이브러리 객체라 @Inject constructor를 붙일 수 없음
    //   → 직접 반환해주는 방식(@Provides)만 가능
    // @IoDispatcher: 이름표. Repository 생성자가 "CoroutineDispatcher 아무거나"가 아니라
    //   "@IoDispatcher가 붙은 것"을 요청하도록 구분해줌 (안 붙이면 Default/Main과 타입이 겹쳐 모호해짐)
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    // 아래 둘은 위와 동일 패턴 — 지금은 IoDispatcher만 실제로 쓰이고, 나머지는 필요해지면 바로 쓸 수 있게 미리 준비해둠
    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
