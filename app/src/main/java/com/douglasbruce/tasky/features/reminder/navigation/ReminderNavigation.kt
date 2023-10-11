package com.douglasbruce.tasky.features.reminder.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.douglasbruce.tasky.features.reminder.ReminderRoute

const val reminderNavigationRoute = "reminder"

fun NavController.navigateToReminder(navOptions: NavOptions? = null) {
    this.navigate(reminderNavigationRoute, navOptions)
}

fun NavGraphBuilder.reminderScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = reminderNavigationRoute,
    ) {
        ReminderRoute(
            onBackClick = onBackClick,
        )
    }
}