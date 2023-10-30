package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.MoshiSerializer
import com.douglasbruce.tasky.core.domain.repository.AuthRepository
import com.douglasbruce.tasky.core.network.model.request.LoginRequest
import com.douglasbruce.tasky.core.network.model.request.RegisterRequest
import com.douglasbruce.tasky.core.network.model.toEntity
import com.douglasbruce.tasky.core.network.retrofit.RetrofitTaskyNetwork
import com.douglasbruce.tasky.core.network.retrofit.authenticatedRetrofitCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val taskyNetwork: RetrofitTaskyNetwork,
    private val serializer: MoshiSerializer,
) : AuthRepository {
    override suspend fun login(email: String, password: String) =
        authenticatedRetrofitCall(serializer) {
            val request = LoginRequest(
                email = email,
                password = password
            )
            val user = taskyNetwork.login(request).toEntity()
            AuthResult.Success(user)
        }


    override suspend fun register(fullName: String, email: String, password: String) =
        authenticatedRetrofitCall(serializer) {
            val request = RegisterRequest(
                fullName = fullName,
                email = email,
                password = password
            )
            taskyNetwork.register(request)
            AuthResult.Success(Unit)
        }

    override suspend fun authenticate() = authenticatedRetrofitCall(serializer) {
        taskyNetwork.authenticate()
        AuthResult.Success(Unit)
    }

    override suspend fun logout() = authenticatedRetrofitCall(serializer) {
        taskyNetwork.logout()
        AuthResult.Success(Unit)
    }
}