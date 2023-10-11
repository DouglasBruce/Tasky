package com.douglasbruce.tasky.core.domain.repository

import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult<User>
    suspend fun register(fullName: String, email: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun logout(): AuthResult<Unit>
}