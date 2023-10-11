package com.douglasbruce.tasky.features.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.douglasbruce.tasky.features.agenda.navigation.agendaGraphRoute
import com.douglasbruce.tasky.features.login.LoginRoute

const val loginGraphRoute = "login_graph"
const val loginNavigationRoute = "login"

fun NavController.navigateToLoginGraph() {
    this.navigate(loginGraphRoute, NavOptions.Builder().setPopUpTo(agendaGraphRoute, inclusive = true).build())
}

fun NavGraphBuilder.loginGraph(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = loginGraphRoute,
        startDestination = loginNavigationRoute,
    ) {
        composable(route = loginNavigationRoute) {
            LoginRoute(
                onLoginClick= onLoginClick,
                onSignUpClick = onSignUpClick,
            )
        }
        nestedGraphs()
    }
}