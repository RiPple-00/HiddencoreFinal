package com.ddasum.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// 앱 실행 시 가장 먼저 실행됨. Activity와는 별개이며 시작과 동시에 Application().onCreate()
// OS에서 이를 실행하여 Hilt Container가 준비되어있게 만듦.
// 상속된 Application 클래스는 앱이 실행되는 동안 유지되며, 앱의 전역 상태를 관리하는 데 사용됨.
// Hilt를 사용하여 의존성 주입을 설정하기 위해 @HiltAndroidApp 어노테이션을 사용.
@HiltAndroidApp
class DdasumApplication : Application()