package com.douglasbruce.tasky.features.event.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.douglasbruce.tasky.features.event.EventRoute

const val eventNavigationRoute = "event"

fun NavController.navigateToEvent(navOptions: NavOptions? = null) {
    this.navigate(eventNavigationRoute, navOptions)
}

fun NavGraphBuilder.eventScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = eventNavigationRoute,
    ) {
        EventRoute(
            onBackClick = onBackClick,
        )
    }
}