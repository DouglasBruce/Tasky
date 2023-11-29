package com.douglasbruce.tasky.features.task.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.douglasbruce.tasky.features.task.TaskRoute

const val taskNavigationRoute = "task"

@VisibleForTesting
internal const val taskIdArg = "taskId"

@VisibleForTesting
internal const val taskDateArg = "taskDate"

@VisibleForTesting
internal const val taskIsEditingArg = "taskIsEditing"

private const val DEEP_LINK_TASK_URI_PATTERN =
    "tasky://task/{$taskDateArg}/{$taskIsEditingArg}?$taskIdArg={$taskIdArg}"

internal class TaskArgs(val taskId: String?, val taskDateMilli: Long, val taskIsEditing: Boolean) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                savedStateHandle[taskIdArg],
                checkNotNull(savedStateHandle[taskDateArg]),
                checkNotNull(savedStateHandle[taskIsEditingArg])
            )
}

fun NavController.navigateToNewTask(dateMilli: Long) {
    this.navigate("$taskNavigationRoute/$dateMilli/true") {
        launchSingleTop = true
    }
}

fun NavController.navigateToTask(dateMilli: Long, taskId: String, isEditing: Boolean) {
    this.navigate("$taskNavigationRoute/$dateMilli/$isEditing?$taskIdArg=$taskId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.taskScreen(
    onBackClick: () -> Unit,
    onEditorClick: (Boolean, String, String) -> Unit,
) {
    composable(
        route = "$taskNavigationRoute/{$taskDateArg}/{$taskIsEditingArg}?$taskIdArg={$taskIdArg}",
        deepLinks = listOf(
            navDeepLink { uriPattern = DEEP_LINK_TASK_URI_PATTERN },
        ),
        arguments = listOf(
            navArgument(taskDateArg) {
                type = NavType.LongType
            },
            navArgument(taskIsEditingArg) {
                type = NavType.BoolType
                defaultValue = false
            },
            navArgument(taskIdArg) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        val taskTitle = it.savedStateHandle.get<String>("task_title") ?: ""
        val taskDescription = it.savedStateHandle.get<String>("task_desc") ?: ""
        TaskRoute(
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            onBackClick = onBackClick,
            onEditorClick = onEditorClick,
        )
    }
}