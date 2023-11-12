package com.douglasbruce.tasky.core.data.database.util

import androidx.room.TypeConverter
import com.douglasbruce.tasky.core.model.Attendee
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class AttendeeConverter {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val attendeeListType =
        Types.newParameterizedType(List::class.java, Attendee::class.java)
    private val jsonAdapter = moshi.adapter<List<Attendee>>(attendeeListType)

    @TypeConverter
    fun fromJson(json: String): List<Attendee> {
        return jsonAdapter.fromJson(json) ?: emptyList()
    }

    @TypeConverter
    fun toJson(listOfAttendee: List<Attendee>): String {
        return jsonAdapter.toJson(listOfAttendee)
    }
}