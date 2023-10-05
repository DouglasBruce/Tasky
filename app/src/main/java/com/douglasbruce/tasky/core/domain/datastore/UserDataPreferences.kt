package com.douglasbruce.tasky.core.domain.datastore

import com.douglasbruce.tasky.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataPreferences {

    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>
}