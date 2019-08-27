package com.github.travelplannerapp.communication

import com.github.travelplannerapp.utils.SharedPreferencesUtils
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SharedPreferencesUtils.getCredentials().token
        val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
        if (token != null)
            request.addHeader("authorization", token)

        val build = request.build()

        return chain.proceed(build)
    }
}