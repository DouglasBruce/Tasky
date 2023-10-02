package com.douglasbruce.tasky.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class JwtInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = null
        val request =
            chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(request)
    }
}