package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.core.data.datastore.TaskyPreferencesDataSource
import com.douglasbruce.tasky.core.domain.repository.UserDataRepository
import com.douglasbruce.tasky.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val taskyPreferencesDataSource: TaskyPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        taskyPreferencesDataSource.userData

    override suspend fun setToken(token: String) = taskyPreferencesDataSource.setToken(token)

    override suspend fun setUserId(userId: String) = taskyPreferencesDataSource.setUserId(userId)

    override suspend fun setFullName(fullName: String) =
        taskyPreferencesDataSource.setFullName(fullName)
}