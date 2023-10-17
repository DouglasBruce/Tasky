package com.douglasbruce.tasky.features.reminder.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.reminder.ReminderRoute

const val reminderNavigationRoute = "reminder"

@VisibleForTesting
internal const val reminderIdArg = "reminderId"

internal class ReminderArgs(val reminderId: String?) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(savedStateHandle[reminderIdArg])
}

fun NavController.navigateToNewReminder() {
    this.navigate("$reminderNavigationRoute/") {
        launchSingleTop = true
    }
}

fun NavController.navigateToReminder(reminderId: String) {
    this.navigate("$reminderNavigationRoute/$reminderId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.reminderScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "$reminderNavigationRoute/?$reminderIdArg={$reminderIdArg}",
        arguments = listOf(
            navArgument(reminderIdArg) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        ReminderRoute(
            onBackClick = onBackClick,
        )
    }
}