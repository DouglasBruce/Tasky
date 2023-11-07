package com.douglasbruce.tasky.core.network.model

import com.squareup.moshi.Json

data class NetworkEvent(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "from")
    val from: Long,
    @field:Json(name = "to")
    val to: Long,
    @field:Json(name = "remindAt")
    val remindAt: Long,
    @field:Json(name = "host")
    val host: String,
    @field:Json(name = "isUserEventCreator")
    val isUserEventCreator: Boolean,
    @field:Json(name = "attendees")
    val attendees: List<NetworkAttendee>,
    @field:Json(name = "photos")
    val photos: List<NetworkPhoto>,
)