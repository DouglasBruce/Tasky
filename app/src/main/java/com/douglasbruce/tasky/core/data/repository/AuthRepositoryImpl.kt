package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.core.domain.repository.AuthRepository
import com.douglasbruce.tasky.core.network.model.toEntity
import com.douglasbruce.tasky.core.network.retrofit.RetrofitTaskyNetwork
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val taskyNetwork: RetrofitTaskyNetwork
) : AuthRepository {
    override suspend fun login(email: String, password: String) =
        taskyNetwork.login(email, password).toEntity()

    override suspend fun register(fullName: String, email: String, password: String) =
        taskyNetwork.register(fullName, email, password)

    override suspend fun authenticate() = taskyNetwork.authenticate()
}