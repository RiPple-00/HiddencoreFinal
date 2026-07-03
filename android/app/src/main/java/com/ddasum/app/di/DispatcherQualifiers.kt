package com.ddasum.app.di

import javax.inject.Qualifier

// @Qualifier: "이 어노테이션은 타입 구분용 이름표다"라고 Hilt/Dagger에게 알려주는 메타 어노테이션.
//   CoroutineDispatcher 타입 하나에 구현체가 3개(IO/Default/Main) 있어서, 타입만으로는
//   Hilt가 "어떤 걸 넣어줘야 할지" 알 수 없음 — 그래서 이름표로 구분한다.
// @Retention(AnnotationRetention.BINARY): 이 이름표는 컴파일된 .class 파일까지만 남고
//   런타임에 리플렉션으로 조회할 필요는 없다는 설정 (Hilt 쓰는 어노테이션의 표준 관례).

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher
