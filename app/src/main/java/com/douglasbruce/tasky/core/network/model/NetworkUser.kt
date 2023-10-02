package com.douglasbruce.tasky.core.network.model

import com.douglasbruce.tasky.core.model.User

data class NetworkUser(
    val token: String,
    val userId: String,
    val fullName: String,
)

fun NetworkUser.toEntity(): User {
    return User(
        token = this.token,
        userId = this.userId,
        fullName = this.fullName
    )
}