package com.douglasbruce.tasky.features.event.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.event.EventRoute
import java.net.URLDecoder
import java.net.URLEncoder

const val eventNavigationRoute = "event"
private val urlCharacterEncoding = Charsets.UTF_8.name()

@VisibleForTesting
internal const val eventIdArg = "eventId"

internal class EventArgs(val eventId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(URLDecoder.decode(checkNotNull(savedStateHandle[eventIdArg]), urlCharacterEncoding))
}

fun NavController.navigateToEvent(eventId: String?) {
    var id = eventId
    if (eventId.isNullOrBlank()) {
        id = "-1"
    }

    val encodedId = URLEncoder.encode(id, urlCharacterEncoding)
    this.navigate("$eventNavigationRoute/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.eventScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "$eventNavigationRoute/{$eventIdArg}",
        arguments = listOf(
            navArgument(eventIdArg) { type = NavType.StringType },
        ),
    ) {
        EventRoute(
            onBackClick = onBackClick,
        )
    }
}
