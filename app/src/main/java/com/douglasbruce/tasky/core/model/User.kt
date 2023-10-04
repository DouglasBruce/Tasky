package com.douglasbruce.tasky.core.model

data class User(
    val token: String,
    val userId: String,
    val fullName: String
)