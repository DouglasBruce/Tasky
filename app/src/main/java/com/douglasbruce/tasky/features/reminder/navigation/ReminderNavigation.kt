package com.douglasbruce.tasky.features.reminder.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.reminder.ReminderRoute
import java.net.URLDecoder
import java.net.URLEncoder

const val reminderNavigationRoute = "reminder"
private val urlCharacterEncoding = Charsets.UTF_8.name()

@VisibleForTesting
internal const val reminderIdArg = "reminderId"

internal class ReminderArgs(val reminderId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(URLDecoder.decode(checkNotNull(savedStateHandle[reminderIdArg]), urlCharacterEncoding))
}

fun NavController.navigateToReminder(reminderId: String) {
    val encodedId = URLEncoder.encode(reminderId, urlCharacterEncoding)
    this.navigate("$reminderNavigationRoute/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.reminderScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "$reminderNavigationRoute/{$reminderIdArg}",
        arguments = listOf(
            navArgument(reminderIdArg) { type = NavType.StringType },
        ),
    ) {
        ReminderRoute(
            onBackClick = onBackClick,
        )
    }
}