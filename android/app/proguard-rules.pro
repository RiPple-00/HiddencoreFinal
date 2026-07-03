# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# ---- Gson ----
# Gson reads field NAMES via reflection to match JSON keys — if R8 renames fields
# (e.g. "mealDate" -> "a"), deserialization silently breaks (fields end up null,
# no exception). All Retrofit response/request DTOs must keep their field names.
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
# Retrofit inspects generic return types (e.g. List<MealPlanResponse>) at runtime.
-keepattributes Exceptions, InnerClasses, EnclosingMethod
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# ---- Kotlin coroutines / suspend functions used through Retrofit ----
-keepclassmembernames class kotlin.coroutines.Continuation
