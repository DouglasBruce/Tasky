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

@VisibleForTesting
internal const val eventIsEditingArg = "eventIsEditing"

internal class EventArgs(
    val eventId: String?,
    val eventToDateMilli: Long,
    val eventFromDateMilli: Long,
    val eventIsEditing: Boolean,
) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                savedStateHandle[eventIdArg],
                checkNotNull(savedStateHandle[eventToDateArg]),
                checkNotNull(savedStateHandle[eventFromDateArg]),
                checkNotNull(savedStateHandle[eventIsEditingArg])
            )
}

fun NavController.navigateToNewEvent(dateMilli: Long) {
    this.navigate("$eventNavigationRoute/$dateMilli/$dateMilli/true") {
        launchSingleTop = true
    }
}

fun NavController.navigateToEvent(toDateMilli: Long, fromDateMilli: Long, eventId: String, isEditing: Boolean) {
    this.navigate("$eventNavigationRoute/$toDateMilli/$fromDateMilli/$isEditing?$eventIdArg=$eventId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.eventScreen(
    onBackClick: () -> Unit,
    onEditorClick: (isTitle: Boolean, key: String, value: String) -> Unit,
    onPhotoViewerClick: (key: String, uri: String) -> Unit,
    onLogoutClick: () -> Unit,
) {
    composable(
        route = "$eventNavigationRoute/{$eventToDateArg}/{$eventFromDateArg}/{$eventIsEditingArg}?$eventIdArg={$eventIdArg}",
        arguments = listOf(
            navArgument(eventToDateArg) {
                type = NavType.LongType
            },
            navArgument(eventFromDateArg) {
                type = NavType.LongType
            },
            navArgument(eventIsEditingArg) {
                type = NavType.BoolType
                defaultValue = false
            },
            navArgument(eventIdArg) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        val eventTitle = it.savedStateHandle.get<String>("event_title") ?: ""
        val eventDescription = it.savedStateHandle.get<String>("event_desc") ?: ""
        val removePhotoLocation = it.savedStateHandle.get<String>("remove_photo") ?: ""
        EventRoute(
            eventTitle = eventTitle,
            eventDescription = eventDescription,
            removePhotoLocation = removePhotoLocation,
            onBackClick = onBackClick,
            onEditorClick = onEditorClick,
            onPhotoViewerClick = onPhotoViewerClick,
            onLogoutClick = onLogoutClick,
        )
    }
}
