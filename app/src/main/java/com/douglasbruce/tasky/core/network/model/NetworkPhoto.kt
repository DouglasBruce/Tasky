package com.douglasbruce.tasky.core.network.model

import com.squareup.moshi.Json

data class NetworkPhoto(
    @field:Json(name = "key")
    val key: String,
    @field:Json(name = "url")
    val url: String,
)
