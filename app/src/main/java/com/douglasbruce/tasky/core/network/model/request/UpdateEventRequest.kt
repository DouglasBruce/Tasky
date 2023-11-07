package com.douglasbruce.tasky.core.network.model.request

import com.squareup.moshi.Json

data class UpdateEventRequest(
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
    @field:Json(name = "attendeeIds")
    val attendeeIds: List<String>,
    @field:Json(name = "deletedPhotoKeys")
    val deletedPhotoKeys: List<String>,
    @field:Json(name = "isGoing")
    val isGoing: Boolean,
)
