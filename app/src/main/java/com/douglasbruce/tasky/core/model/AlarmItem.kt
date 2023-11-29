package com.douglasbruce.tasky.core.model

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val title: String,
    val text: String,
    val id: String,
    val date: Long,
    val type: String,
)
