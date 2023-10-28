package com.douglasbruce.tasky.core.network.model.request

import com.squareup.moshi.Json

data class SyncAgendaRequest(
    @field:Json(name = "deletedEventIds")
    val deletedEventIds: List<String>,
    @field:Json(name = "deletedTaskIds")
    val deletedTaskIds: List<String>,
    @field:Json(name = "deletedReminderIds")
    val deletedReminderIds: List<String>,
)