package com.douglasbruce.tasky.core.network.model

import com.squareup.moshi.Json

data class RegisterRequest(
    @field:Json(name = "fullName")
    val fullName: String,
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "password")
    val password: String
)