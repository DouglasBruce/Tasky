package com.douglasbruce.tasky.features.reminder.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.douglasbruce.tasky.features.reminder.ReminderRoute

const val reminderNavigationRoute = "reminder"

@VisibleForTesting
internal const val reminderIdArg = "reminderId"

@VisibleForTesting
internal const val reminderDateArg = "reminderDate"

@VisibleForTesting
internal const val reminderIsEditingArg = "reminderIsEditing"

private const val DEEP_LINK_REMINDER_URI_PATTERN =
    "tasky://reminder/{$reminderDateArg}/{$reminderIsEditingArg}?$reminderIdArg={$reminderIdArg}"

internal class ReminderArgs(
    val reminderId: String?,
    val reminderDateMilli: Long,
    val reminderIsEditing: Boolean,
) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                savedStateHandle[reminderIdArg],
                checkNotNull(savedStateHandle[reminderDateArg]),
                checkNotNull(savedStateHandle[reminderIsEditingArg])
            )
}

fun NavController.navigateToNewReminder(dateMilli: Long) {
    this.navigate("$reminderNavigationRoute/$dateMilli/true") {
        launchSingleTop = true
    }
}

fun NavController.navigateToReminder(dateMilli: Long, reminderId: String, isEditing: Boolean) {
    this.navigate("$reminderNavigationRoute/$dateMilli/$isEditing?$reminderIdArg=$reminderId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.reminderScreen(
    onBackClick: () -> Unit,
    onEditorClick: (Boolean, String, String) -> Unit,
) {
    composable(
        route = "$reminderNavigationRoute/{$reminderDateArg}/{$reminderIsEditingArg}?$reminderIdArg={$reminderIdArg}",
        deepLinks = listOf(
            navDeepLink { uriPattern = DEEP_LINK_REMINDER_URI_PATTERN },
        ),
        arguments = listOf(
            navArgument(reminderDateArg) {
                type = NavType.LongType
            },
            navArgument(reminderIsEditingArg) {
                type = NavType.BoolType
                defaultValue = false
            },
            navArgument(reminderIdArg) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        val reminderTitle = it.savedStateHandle.get<String>("reminder_title") ?: ""
        val reminderDescription = it.savedStateHandle.get<String>("reminder_desc") ?: ""
        ReminderRoute(
            reminderTitle = reminderTitle,
            reminderDescription = reminderDescription,
            onBackClick = onBackClick,
            onEditorClick = onEditorClick,
        )
    }
}