package com.douglasbruce.tasky.core.domain.utils

import com.douglasbruce.tasky.core.model.AlarmItem

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    suspend fun scheduleAllFutureAlarms()
    fun cancel(id: String)
    fun cancelAll()
}