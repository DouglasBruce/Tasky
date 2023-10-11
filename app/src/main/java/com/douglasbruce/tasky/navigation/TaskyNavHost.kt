package com.douglasbruce.tasky.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.douglasbruce.tasky.features.agenda.navigation.agendaGraph
import com.douglasbruce.tasky.features.agenda.navigation.navigateToAgendaGraph
import com.douglasbruce.tasky.features.login.navigation.loginGraph
import com.douglasbruce.tasky.features.login.navigation.loginGraphRoute
import com.douglasbruce.tasky.features.register.navigation.navigateToRegister
import com.douglasbruce.tasky.features.register.navigation.registerScreen
import com.douglasbruce.tasky.features.task.navigation.navigateToTask
import com.douglasbruce.tasky.features.task.navigation.taskScreen

@Composable
fun TaskyNavHost(
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = loginGraphRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        loginGraph(
            onLoginClick = navController::navigateToAgendaGraph,
            onSignUpClick = navController::navigateToRegister,
            nestedGraphs = {
                registerScreen(
                    onBackClick = navController::popBackStack
                )
            }
        )
        agendaGraph(
            onLogoutClick = onLogoutClick,
            onAddTaskClick = navController::navigateToTask,
            nestedGraphs = {
                taskScreen(
                    onBackClick = navController::popBackStack
                )
            }
        )
    }
}