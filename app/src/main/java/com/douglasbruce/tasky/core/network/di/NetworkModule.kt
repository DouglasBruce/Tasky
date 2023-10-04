package com.douglasbruce.tasky.core.network.di

import androidx.datastore.core.DataStore
import com.douglasbruce.tasky.BuildConfig
import com.douglasbruce.tasky.UserPreferences
import com.douglasbruce.tasky.core.network.interceptor.ApiKeyInterceptor
import com.douglasbruce.tasky.core.network.interceptor.JwtInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun okHttpCallFactory(userPreferences: DataStore<UserPreferences>): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .addInterceptor(ApiKeyInterceptor())
        .addInterceptor(JwtInterceptor(userPreferences))
        .build()
}