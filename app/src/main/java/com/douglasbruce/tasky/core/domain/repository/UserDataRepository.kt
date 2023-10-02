package com.douglasbruce.tasky.core.domain.repository

import com.douglasbruce.tasky.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the user's token
     */
    suspend fun setToken(token: String)

    /**
     * Sets the user's id
     */
    suspend fun setUserId(userId: String)

    /**
     * Sets the user's full name
     */
    suspend fun setFullName(fullName: String)
}