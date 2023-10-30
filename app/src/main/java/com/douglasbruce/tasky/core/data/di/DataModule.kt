package com.douglasbruce.tasky.core.data.di

import com.douglasbruce.tasky.core.common.utils.MoshiSerializer
import com.douglasbruce.tasky.core.data.auth.SessionManagerImpl
import com.douglasbruce.tasky.core.data.datastore.TaskyPreferencesDataSource
import com.douglasbruce.tasky.core.data.repository.AgendaRepositoryImpl
import com.douglasbruce.tasky.core.data.repository.AuthRepositoryImpl
import com.douglasbruce.tasky.core.data.repository.OfflineFirstUserDataRepository
import com.douglasbruce.tasky.core.data.repository.TaskRepositoryImpl
import com.douglasbruce.tasky.core.data.validation.EmailMatcherImpl
import com.douglasbruce.tasky.core.domain.auth.SessionManager
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.repository.AgendaRepository
import com.douglasbruce.tasky.core.domain.repository.AuthRepository
import com.douglasbruce.tasky.core.domain.repository.TaskRepository
import com.douglasbruce.tasky.core.domain.repository.UserDataRepository
import com.douglasbruce.tasky.core.domain.utils.JsonSerializer
import com.douglasbruce.tasky.core.domain.validation.EmailMatcher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsEmailMatcher(
        emailMatcher: EmailMatcherImpl,
    ): EmailMatcher

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository,
    ): UserDataRepository

    @Binds
    fun bindsAuthRepository(
        authRepository: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    fun bindsUserDataPreferences(
        userDataPreferences: TaskyPreferencesDataSource,
    ): UserDataPreferences

    @Binds
    fun bindsMoshiSerializer(
        serializer: MoshiSerializer,
    ): JsonSerializer

    @Binds
    fun bindsSessionManager(
        sessionManager: SessionManagerImpl,
    ): SessionManager

    @Binds
    fun bindsAgendaRepository(
        agendaRepositoryImpl: AgendaRepositoryImpl
    ): AgendaRepository

    @Binds
    fun bindsTaskRepository(
        taskRepository: TaskRepositoryImpl,
    ): TaskRepository
}