package com.douglasbruce.tasky.core.domain.mapper

import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.network.model.NetworkAgenda

fun NetworkAgenda.toAgendaItems(): List<AgendaItem> {
    return this.tasks.map { it.toTask() } //TODO: Update to handle events and reminders
}