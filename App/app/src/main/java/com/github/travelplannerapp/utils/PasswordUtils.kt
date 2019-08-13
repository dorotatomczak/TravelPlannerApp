package com.github.travelplannerapp.utils

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class PasswordUtils {
    private val RAND = SecureRandom()
    private val ITERATIONS = 65536
    private val KEY_LENGTH = 32
    private val SALT_LENGTH = 32
    private val ALGORITHM = "PBKDF2WithHmacSHA512"

    fun generateSalt(): String? {
        val salt = ByteArray(SALT_LENGTH)
        RAND.nextBytes(salt)

        return android.util.Base64.encodeToString(salt, 0)
    }

    fun hashPassword(password: String): String? {
        var salt = generateSalt()
        val chars = password.toCharArray()
        val bytes = salt?.toByteArray()

        val spec = PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH)

        Arrays.fill(chars, Character.MIN_VALUE)

        try {
            val fac = SecretKeyFactory.getInstance(ALGORITHM)
            val securePassword = fac.generateSecret(spec).encoded
            return android.util.Base64.encodeToString(securePassword, 0)

        } catch (ex: NoSuchAlgorithmException) {
            System.err.println("Exception encountered in hashPassword()")
            return null

        } catch (ex: InvalidKeySpecException) {
            System.err.println("Exception encountered in hashPassword()")
            return null
        } finally {
            spec.clearPassword()
        }
    }
}