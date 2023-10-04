package com.douglasbruce.tasky.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.douglasbruce.tasky.features.agenda.navigation.agendaGraph
import com.douglasbruce.tasky.features.login.navigation.loginGraph
import com.douglasbruce.tasky.features.login.navigation.loginGraphRoute
import com.douglasbruce.tasky.features.register.navigation.navigateToRegister
import com.douglasbruce.tasky.features.register.navigation.registerScreen

@Composable
fun TaskyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = loginGraphRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        loginGraph(
            onSignUpClick = navController::navigateToRegister,
            nestedGraphs = {
                registerScreen(
                    onBackClick = navController::popBackStack
                )
            }
        )
        agendaGraph(
            nestedGraphs = {

            }
        )
    }
}