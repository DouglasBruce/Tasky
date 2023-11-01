package com.douglasbruce.tasky.core.domain.mapper

import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.network.model.NetworkAgenda

fun NetworkAgenda.toAgendaItems(): List<AgendaItem> {
    val events = this.events.map { it.toEvent() }
    val tasks = this.tasks.map { it.toTask() }
    val reminders = this.reminders.map { it.toReminder() }
    return (events + tasks + reminders)
}