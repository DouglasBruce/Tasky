package com.douglasbruce.tasky.core.domain.repository

import com.douglasbruce.tasky.core.model.AgendaItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface AgendaRepository {
    fun getAgendaForDate(date: LocalDate): Flow<List<AgendaItem>>
    suspend fun fetchAgendaForDate(date: LocalDate)
}