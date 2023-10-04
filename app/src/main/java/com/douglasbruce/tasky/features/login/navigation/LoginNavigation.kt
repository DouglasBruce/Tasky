package com.douglasbruce.tasky.features.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.douglasbruce.tasky.features.login.LoginRoute

const val loginGraphRoute = "login_graph"
const val loginNavigationRoute = "login"

fun NavGraphBuilder.loginGraph(
    onSignUpClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = loginGraphRoute,
        startDestination = loginNavigationRoute,
    ) {
        composable(route = loginNavigationRoute) {
            LoginRoute(
                onSignUpClick = onSignUpClick,
            )
        }
        nestedGraphs()
    }
}