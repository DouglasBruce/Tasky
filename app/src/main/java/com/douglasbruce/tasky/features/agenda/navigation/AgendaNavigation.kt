package com.douglasbruce.tasky.features.agenda.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.douglasbruce.tasky.features.agenda.AgendaRoute
import com.douglasbruce.tasky.features.login.navigation.loginGraphRoute

const val agendaGraphRoute = "agenda_graph"
const val agendaNavigationRoute = "agenda"

fun NavController.navigateToAgendaGraph() {
    this.navigate(agendaGraphRoute, NavOptions.Builder().setPopUpTo(loginGraphRoute, inclusive = true).build())
}

fun NavGraphBuilder.agendaGraph(
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = agendaGraphRoute,
        startDestination = agendaNavigationRoute,
    ) {
        composable(route = agendaNavigationRoute) {
            AgendaRoute()
        }
        nestedGraphs()
    }
}