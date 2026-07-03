package com.ddasum.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DataStore는 같은 파일을 두 번 열면 IllegalStateException이 나서, 인스턴스 자체가
 * 싱글턴이어야 한다 — `by preferencesDataStore(...)` Context 확장 프로퍼티 패턴 대신
 * 여기서 제공하면, 테스트에서도 SessionStore에 가짜 DataStore를 넣어줄 수 있다(디스크 안 건드림).
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    // @ApplicationContext: "액티비티 Context 말고, 앱 전체 수명과 같이 가는 Context를 달라"는 요청.
    //   Hilt가 자동으로 Application 객체를 넣어줌 (개발자가 직접 넘길 필요 없음).
    // @Provides + @Singleton: DataStore 파일을 여기서 딱 1번만 열고, 이후 요청은 이 인스턴스를 재사용.
    @Provides
    @Singleton
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("session") }
        )
}
