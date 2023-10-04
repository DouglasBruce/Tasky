package com.douglasbruce.tasky.core.network

import com.douglasbruce.tasky.core.network.model.NetworkUser

interface TaskyNetworkDataSource {
    suspend fun login(email: String, password: String): NetworkUser
    suspend fun register(fullName: String, email: String, password: String)
    suspend fun authenticate()
}