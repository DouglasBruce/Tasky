package com.douglasbruce.tasky.features.event.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.event.EventRoute

const val eventNavigationRoute = "event"

@VisibleForTesting
internal const val eventIdArg = "eventId"

@VisibleForTesting
internal const val eventFromDateArg = "eventFromDate"

@VisibleForTesting
internal const val eventToDateArg = "eventToDate"

internal class EventArgs(
    val eventId: String?,
    val eventToDateMilli: Long,
    val eventFromDateMilli: Long
) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                savedStateHandle[eventIdArg],
                checkNotNull(savedStateHandle[eventToDateArg]),
                checkNotNull(savedStateHandle[eventFromDateArg])
            )
}

fun NavController.navigateToNewEvent(dateMilli: Long) {
    this.navigate("$eventNavigationRoute/$dateMilli/$dateMilli") {
        launchSingleTop = true
    }
}

fun NavController.navigateToEvent(eventId: String) {
    this.navigate("$eventNavigationRoute/$eventId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.eventScreen(
    onBackClick: () -> Unit,
    onEditorClick: (Boolean, String, String) -> Unit,
) {
    composable(
        route = "$eventNavigationRoute/{$eventToDateArg}/{$eventFromDateArg}?$eventIdArg={$eventIdArg}",
        arguments = listOf(
            navArgument(eventToDateArg) {
                type = NavType.LongType
            },
            navArgument(eventFromDateArg) {
                type = NavType.LongType
            },
            navArgument(eventIdArg) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        val eventTitle = it.savedStateHandle.get<String>("event_title") ?: ""
        val eventDescription = it.savedStateHandle.get<String>("event_desc") ?: ""
        EventRoute(
            eventTitle = eventTitle,
            eventDescription = eventDescription,
            onBackClick = onBackClick,
            onEditorClick = onEditorClick,
        )
    }
}
