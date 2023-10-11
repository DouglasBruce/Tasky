package com.douglasbruce.tasky.core.data.auth

import com.douglasbruce.tasky.core.domain.auth.SessionManager
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionManagerImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDataPreferences: UserDataPreferences,
): SessionManager {

    override suspend fun logout() {
        withContext(Dispatchers.IO + NonCancellable) {
            authRepository.logout()
            userDataPreferences.clearPreferences()
            //TODO: Clear room database once implemented
        }
    }
}