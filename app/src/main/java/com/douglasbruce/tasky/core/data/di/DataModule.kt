package com.douglasbruce.tasky.core.data.di

import com.douglasbruce.tasky.core.data.repository.OfflineFirstUserDataRepository
import com.douglasbruce.tasky.core.data.repository.UserDataRepository
import com.douglasbruce.tasky.core.data.validation.EmailMatcherImpl
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
}