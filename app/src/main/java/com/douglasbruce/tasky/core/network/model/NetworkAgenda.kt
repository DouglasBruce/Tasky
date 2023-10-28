package com.douglasbruce.tasky.core.network.model

import com.douglasbruce.tasky.core.model.AgendaItem
import com.squareup.moshi.Json

data class NetworkAgenda(
    @field:Json(name = "events")
    val events: List<AgendaItem.Event>,
    @field:Json(name = "tasks")
    val tasks: List<AgendaItem.Task>,
    @field:Json(name = "reminders")
    val reminders: List<AgendaItem.Reminder>,
)