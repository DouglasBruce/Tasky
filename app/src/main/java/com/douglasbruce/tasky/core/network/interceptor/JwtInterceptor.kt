package com.douglasbruce.tasky.core.network.interceptor

import androidx.datastore.core.DataStore
import com.douglasbruce.tasky.UserPreferences
import kotlinx.coroutines.flow.map
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class JwtInterceptor @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = userPreferences.data.map { it.token }
        val request =
            chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(request)
    }
}