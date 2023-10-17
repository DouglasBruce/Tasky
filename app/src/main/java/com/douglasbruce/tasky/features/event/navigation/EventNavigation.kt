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

internal class EventArgs(val eventId: String?) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(savedStateHandle[eventIdArg])
}

fun NavController.navigateToNewEvent() {
    this.navigate("$eventNavigationRoute/") {
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
) {
    composable(
        route = "$eventNavigationRoute/?$eventIdArg={$eventIdArg}",
        arguments = listOf(
            navArgument(eventIdArg) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        EventRoute(
            onBackClick = onBackClick,
        )
    }
}
