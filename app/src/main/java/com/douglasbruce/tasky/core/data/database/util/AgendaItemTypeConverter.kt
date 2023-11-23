package com.douglasbruce.tasky.core.data.database.util

import androidx.room.TypeConverter
import com.douglasbruce.tasky.core.model.AgendaItemType

class AgendaItemTypeConverter {
    @TypeConverter
    fun agendaItemTypeToString(agendaItemType: AgendaItemType): String =
        agendaItemType.name

    @TypeConverter
    fun stringToAgendaItemType(string: String): AgendaItemType =
        AgendaItemType.valueOf(string)
}