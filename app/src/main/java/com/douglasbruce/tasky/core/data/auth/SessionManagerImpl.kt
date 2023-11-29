package com.douglasbruce.tasky.core.data.auth

import com.douglasbruce.tasky.core.data.database.TaskyDatabase
import com.douglasbruce.tasky.core.domain.auth.SessionManager
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.repository.AuthRepository
import com.douglasbruce.tasky.core.domain.utils.AlarmScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionManagerImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDataPreferences: UserDataPreferences,
    private val taskyDatabase: TaskyDatabase,
    private val alarmScheduler: AlarmScheduler,
) : SessionManager {

    override suspend fun logout() {
        withContext(Dispatchers.IO + NonCancellable) {
            authRepository.logout()
            userDataPreferences.clearPreferences()
            taskyDatabase.clearAllTables()
            alarmScheduler.cancelAll()
        }
    }
}