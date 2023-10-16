package com.douglasbruce.tasky.features.task.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.task.TaskRoute
import java.net.URLDecoder
import java.net.URLEncoder

const val taskNavigationRoute = "task"
private val urlCharacterEncoding = Charsets.UTF_8.name()

@VisibleForTesting
internal const val taskIdArg = "taskId"

internal class TaskArgs(val taskId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(URLDecoder.decode(checkNotNull(savedStateHandle[taskIdArg]), urlCharacterEncoding))
}

fun NavController.navigateToTask(taskId: String?) {
    var id = taskId
    if (taskId.isNullOrBlank()) {
        id = "-1"
    }

    val encodedId = URLEncoder.encode(id, urlCharacterEncoding)
    this.navigate("$taskNavigationRoute/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.taskScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "$taskNavigationRoute/{$taskIdArg}",
        arguments = listOf(
            navArgument(taskIdArg) { type = NavType.StringType },
        ),
    ) {
        TaskRoute(
            onBackClick = onBackClick,
        )
    }
}