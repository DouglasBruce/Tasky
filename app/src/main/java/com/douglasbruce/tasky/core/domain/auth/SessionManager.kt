package com.douglasbruce.tasky.core.domain.auth

interface SessionManager {
    suspend fun logout()
}