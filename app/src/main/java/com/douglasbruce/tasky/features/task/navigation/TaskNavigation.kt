package com.douglasbruce.tasky.features.task.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.task.TaskRoute

const val taskNavigationRoute = "task"

@VisibleForTesting
internal const val taskIdArg = "taskId"

@VisibleForTesting
internal const val taskDateArg = "taskDate"

internal class TaskArgs(val taskId: String?, val taskDateMilli: Long) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(savedStateHandle[taskIdArg], checkNotNull(savedStateHandle[taskDateArg]))
}

fun NavController.navigateToNewTask(dateMilli: Long) {
    this.navigate("$taskNavigationRoute/$dateMilli") {
        launchSingleTop = true
    }
}

fun NavController.navigateToTask(taskId: String) {
    this.navigate("$taskNavigationRoute/$taskId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.taskScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "$taskNavigationRoute/{$taskDateArg}?$taskIdArg={$taskIdArg}",
        arguments = listOf(
            navArgument(taskDateArg) {
                type = NavType.LongType
            },
            navArgument(taskIdArg) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        TaskRoute(
            onBackClick = onBackClick,
        )
    }
}