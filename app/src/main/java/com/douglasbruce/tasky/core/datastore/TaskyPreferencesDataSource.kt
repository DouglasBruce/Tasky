package com.douglasbruce.tasky.core.datastore

import androidx.datastore.core.DataStore
import com.douglasbruce.tasky.UserPreferences
import com.douglasbruce.tasky.copy
import com.douglasbruce.tasky.core.model.data.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskyPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map {
            UserData(
                token = it.token,
                userId = it.userId,
                fullName = it.fullName,
            )
        }

    suspend fun setToken(token: String) {
        userPreferences.updateData {
            it.copy {
                this.token = token
            }
        }
    }

    suspend fun setUserId(userId: String) {
        userPreferences.updateData {
            it.copy {
                this.userId = userId
            }
        }
    }

    suspend fun setFullName(fullName: String) {
        userPreferences.updateData {
            it.copy {
                this.fullName = fullName
            }
        }
    }
}