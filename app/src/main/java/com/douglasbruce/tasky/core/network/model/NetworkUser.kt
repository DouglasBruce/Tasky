package com.douglasbruce.tasky.core.network.model

import com.douglasbruce.tasky.core.model.User
import com.squareup.moshi.Json

data class NetworkUser(
    @field:Json(name = "token")
    val token: String,
    @field:Json(name = "userId")
    val userId: String,
    @field:Json(name = "fullName")
    val fullName: String,
)

fun NetworkUser.toEntity(): User {
    return User(
        token = this.token,
        userId = this.userId,
        fullName = this.fullName
    )
}