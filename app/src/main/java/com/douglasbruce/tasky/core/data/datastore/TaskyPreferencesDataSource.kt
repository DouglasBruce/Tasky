package com.douglasbruce.tasky.core.data.datastore

import androidx.datastore.core.DataStore
import com.douglasbruce.tasky.UserPreferences
import com.douglasbruce.tasky.copy
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskyPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
): UserDataPreferences {

    override val userData = userPreferences.data
        .map {
            UserData(
                token = it.token,
                userId = it.userId,
                fullName = it.fullName,
            )
        }

    override suspend fun clearPreferences() {
        userPreferences.updateData {
            it.copy {
                this.token = ""
                this.userId = ""
                this.fullName = ""
            }
        }
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