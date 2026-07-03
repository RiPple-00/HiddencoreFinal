package com.ddasum.app.data.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 기기의 보안 하드웨어(Android Keystore) 안에서 생성·보관되는 AES 키로 값을 암호화한다 —
 * 키 원본은 이 프로세스 메모리로도 나오지 않고 Keystore 밖으로 절대 안 나간다.
 * DataStore는 자체 암호화가 없어서, 이게 없으면 accessToken이 디스크에 평문으로 남는다.
 */
@Singleton
class TokenCipher @Inject constructor() {

    private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    private fun getOrCreateKey(): SecretKey {
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
        )
        return keyGenerator.generateKey()
    }

    /** 결과는 "<base64 iv>:<base64 암호문>" 형태 — GCM은 복호화할 때 IV가 다시 필요하다. */
    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val cipherBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val iv = Base64.encodeToString(cipher.iv, Base64.NO_WRAP)
        val cipherText = Base64.encodeToString(cipherBytes, Base64.NO_WRAP)
        return "$iv:$cipherText"
    }

    /** 실패하면(값 손상, 키 교체 등) null — 호출하는 쪽은 이걸 "세션 없음"으로 취급한다. */
    fun decrypt(encrypted: String): String? = runCatching {
        val (ivPart, cipherPart) = encrypted.split(":", limit = 2)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateKey(),
            GCMParameterSpec(GCM_TAG_LENGTH_BITS, Base64.decode(ivPart, Base64.NO_WRAP))
        )
        val plainBytes = cipher.doFinal(Base64.decode(cipherPart, Base64.NO_WRAP))
        String(plainBytes, Charsets.UTF_8)
    }.getOrNull()

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "ddasum_session_token_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_TAG_LENGTH_BITS = 128
    }
}
