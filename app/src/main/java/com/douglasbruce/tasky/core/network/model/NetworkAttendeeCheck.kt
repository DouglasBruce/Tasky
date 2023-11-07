package com.douglasbruce.tasky.core.network.model

import com.squareup.moshi.Json

data class NetworkAttendeeCheck(
    @field:Json(name = "doesUserExist")
    val doesUserExist: Boolean,
    @field:Json(name = "attendee")
    val attendee: NetworkAttendee,
)