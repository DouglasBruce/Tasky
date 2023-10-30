package com.douglasbruce.tasky.core.network.model

import com.squareup.moshi.Json

data class NetworkAgenda(
    @field:Json(name = "events")
    val events: List<NetworkEvent>,
    @field:Json(name = "tasks")
    val tasks: List<NetworkTask>,
    @field:Json(name = "reminders")
    val reminders: List<NetworkReminder>,
)