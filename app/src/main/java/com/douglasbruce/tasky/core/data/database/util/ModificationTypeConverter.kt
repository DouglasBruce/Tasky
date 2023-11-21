package com.douglasbruce.tasky.core.data.database.util

import androidx.room.TypeConverter
import com.douglasbruce.tasky.core.model.ModificationType

class ModificationTypeConverter {
    @TypeConverter
    fun modificationTypeToString(modificationType: ModificationType): String =
        modificationType.name

    @TypeConverter
    fun stringToModificationType(string: String): ModificationType =
        ModificationType.valueOf(string)
}