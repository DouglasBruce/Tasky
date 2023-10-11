package com.douglasbruce.tasky.features.task.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.douglasbruce.tasky.features.task.TaskRoute

const val taskNavigationRoute = "task"

fun NavController.navigateToTask(navOptions: NavOptions? = null) {
    this.navigate(taskNavigationRoute, navOptions)
}

fun NavGraphBuilder.taskScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = taskNavigationRoute,
    ) {
        TaskRoute(
            onBackClick = onBackClick,
        )
    }
}