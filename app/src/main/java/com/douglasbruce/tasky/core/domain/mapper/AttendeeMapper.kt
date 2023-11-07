package com.douglasbruce.tasky.core.domain.mapper

import com.douglasbruce.tasky.core.model.Attendee
import com.douglasbruce.tasky.core.network.model.NetworkEventAttendee

fun NetworkEventAttendee.toAttendee(): Attendee {
    return Attendee(
        userId = this.userId,
        email = this.email,
        fullName = this.fullName,
        remindAt = this.remindAt,
        eventId = this.eventId,
        isGoing = this.isGoing,
    )
}

fun List<NetworkEventAttendee>.toAttendees(): List<Attendee> {
    return this.map { it.toAttendee() }
}