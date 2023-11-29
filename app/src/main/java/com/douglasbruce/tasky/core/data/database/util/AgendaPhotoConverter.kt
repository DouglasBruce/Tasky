package com.douglasbruce.tasky.core.data.database.util

import android.net.Uri
import androidx.room.TypeConverter
import com.douglasbruce.tasky.core.model.AgendaPhoto
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class AgendaPhotoConverter {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(UriJsonAdapter())
        .build()
    private val photoListType =
        Types.newParameterizedType(List::class.java, AgendaPhoto.Local::class.java)
    private val jsonAdapter = moshi.adapter<List<AgendaPhoto.Local>>(photoListType)

    @TypeConverter
    fun fromJson(json: String): List<AgendaPhoto.Local> {
        return jsonAdapter.fromJson(json) ?: emptyList()
    }

    @TypeConverter
    fun toJson(listOfPhotos: List<AgendaPhoto.Local>): String {
        return jsonAdapter.toJson(listOfPhotos)
    }
}

class UriJsonAdapter : JsonAdapter<Uri>() {
    @ToJson
    override fun toJson(writer: JsonWriter, value: Uri?) {
        writer.value(value.toString())
    }

    @FromJson
    override fun fromJson(reader: JsonReader): Uri {
        return Uri.parse(reader.nextString())
    }
}