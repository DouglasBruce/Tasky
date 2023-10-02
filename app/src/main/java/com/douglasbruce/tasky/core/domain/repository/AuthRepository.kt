package com.douglasbruce.tasky.core.domain.repository

import com.douglasbruce.tasky.core.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(fullName: String, email: String, password: String)
    suspend fun authenticate()
}