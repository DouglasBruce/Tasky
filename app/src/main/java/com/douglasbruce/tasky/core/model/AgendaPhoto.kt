package com.douglasbruce.tasky.core.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
sealed interface AgendaPhoto: Parcelable {

    @Parcelize
    data class Remote(
        val key: String,
        val photoUri: String,
    ): AgendaPhoto, Parcelable

    @Parcelize
    data class Local(
        val key: String = UUID.randomUUID().toString(),
        val uri: Uri,
    ): AgendaPhoto, Parcelable

    fun key(): String {
        return when (this) {
            is Remote -> key
            is Local -> key
        }
    }

    fun uri(): String {
        return when (this) {
            is Remote -> photoUri
            is Local -> uri.toString()
        }
    }
}