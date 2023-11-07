package com.douglasbruce.tasky.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attendee(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
) : Parcelable
