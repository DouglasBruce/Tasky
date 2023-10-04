package com.douglasbruce.tasky.core.network.retrofit

import com.douglasbruce.tasky.BuildConfig
import com.douglasbruce.tasky.core.network.TaskyNetworkDataSource
import com.douglasbruce.tasky.core.network.model.LoginRequest
import com.douglasbruce.tasky.core.network.model.NetworkUser
import com.douglasbruce.tasky.core.network.model.RegisterRequest
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitTaskyNetworkApi {
    @POST("/login")
    suspend fun login(
        @Body body: LoginRequest
    ): NetworkUser

    @POST("/register")
    suspend fun register(
        @Body body: RegisterRequest
    )

    @GET("/authenticate")
    suspend fun authenticate()
}

private const val TASKY_BASE_URL = BuildConfig.BACKEND_URL

@Singleton
class RetrofitTaskyNetwork @Inject constructor(
    okHttpCallFactory: Call.Factory,
) : TaskyNetworkDataSource {

    private val networkApi =
        Retrofit.Builder()
            .baseUrl(TASKY_BASE_URL)
            .callFactory(okHttpCallFactory)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RetrofitTaskyNetworkApi::class.java)

    override suspend fun login(email: String, password: String): NetworkUser {
        val request = LoginRequest(
            email = email,
            password = password
        )
        return networkApi.login(request)
    }

    override suspend fun register(fullName: String, email: String, password: String) {
        val request = RegisterRequest(
            fullName = fullName,
            email = email,
            password = password
        )
        networkApi.register(request)
    }

    override suspend fun authenticate() {
        networkApi.authenticate()
    }
}