package com.douglasbruce.tasky.core.domain.notifications

/**
 * Interface for creating notifications in the app
 */
interface Notifier {
    fun postAgendaNotifications(id: String, title: String, text: String, date: Long, type: String)
}