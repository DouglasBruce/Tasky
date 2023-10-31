package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.core.domain.mapper.toAgendaItems
import com.douglasbruce.tasky.core.domain.repository.AgendaRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.network.retrofit.RetrofitTaskyNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    private val taskyNetwork: RetrofitTaskyNetwork,
) : AgendaRepository {
    override suspend fun getAgendaForDate(date: LocalDate): Flow<List<AgendaItem>> {
        //TODO: Read locally first
        return flow {
            val zone = ZonedDateTime.now().zone
            val agendaItems = taskyNetwork.getAgenda(
                timeZone = zone.toString(),
                time = ZonedDateTime.of(date, LocalTime.now(), zone).toEpochSecond() * 1000
            ).toAgendaItems()

            emit(agendaItems)
        }
    }
}