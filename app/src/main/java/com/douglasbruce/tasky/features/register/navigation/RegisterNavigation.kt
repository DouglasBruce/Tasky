package com.douglasbruce.tasky.features.register.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.douglasbruce.tasky.features.register.RegisterRoute

const val registerNavigationRoute = "register"

fun NavController.navigateToRegister(navOptions: NavOptions? = null) {
    this.navigate(registerNavigationRoute, navOptions)
}

fun NavGraphBuilder.registerScreen(
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(
        route = registerNavigationRoute,
    ) {
        RegisterRoute(
            onRegisterClick = onRegisterClick,
            onBackClick = onBackClick,
        )
    }
}