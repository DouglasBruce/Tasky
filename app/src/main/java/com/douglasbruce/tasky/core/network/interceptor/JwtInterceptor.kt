package com.douglasbruce.tasky.core.network.interceptor

import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private val _onLoggedOut = MutableSharedFlow<Unit>(replay = 3)
val onLoggedOut = _onLoggedOut.asSharedFlow()

class JwtInterceptor @Inject constructor(
    private val userDataPreferences: UserDataPreferences,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            userDataPreferences.userData.map { it.token }.first()
        }
        val request =
            chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(request).also { response ->
            if (response.code == 401) {
                runBlocking {
                    _onLoggedOut.emit(Unit)
                }
            }
        }
    }
}