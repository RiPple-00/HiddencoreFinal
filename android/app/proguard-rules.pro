# 프로젝트 전용 ProGuard 규칙을 여기에 추가한다.
# 어떤 설정 파일들이 적용되는지는 build.gradle의 proguardFiles로 제어한다.
#
# 자세한 내용은 아래 참고:
#   http://developer.android.com/guide/developing/tools/proguard.html

# 스택 트레이스 디버깅용으로 줄 번호 정보를 남기고 싶으면 주석 해제
#-keepattributes SourceFile,LineNumberTable

# ---- Gson ----
# Gson은 JSON 키와 매칭할 때 필드 "이름"을 리플렉션으로 그대로 읽는다 — R8이 필드명을
# 바꿔버리면(예: "mealDate" -> "a") 역직렬화가 예외 없이 조용히 깨진다(필드가 null이 됨).
# Retrofit 요청/응답 DTO는 전부 필드명을 그대로 유지해야 한다.
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.ddasum.app.data.remote.dto.** { <fields>; }
-keep class com.ddasum.app.data.remote.dto.**$* { <fields>; }

-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# ---- Retrofit ----
# Retrofit은 런타임에 제네릭 반환 타입(예: List<MealPlanResponse>)을 리플렉션으로 읽는다.
-keepattributes Exceptions, InnerClasses, EnclosingMethod
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# ---- Retrofit이 쓰는 Kotlin 코루틴/suspend 함수 ----
-keepclassmembernames class kotlin.coroutines.Continuation
