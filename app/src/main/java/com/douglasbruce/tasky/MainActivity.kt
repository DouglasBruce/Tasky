package com.douglasbruce.tasky

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.features.agenda.navigation.agendaGraphRoute
import com.douglasbruce.tasky.features.login.navigation.loginGraphRoute
import com.douglasbruce.tasky.navigation.TaskyNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        setContent {
            TaskyApp(viewModel)
        }
    }
}

@Composable
fun TaskyApp(viewModel: MainActivityViewModel) {
    TaskyTheme {
        val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

        if (!isLoading) {
            val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()
            val navController: NavHostController = rememberNavController()

            TaskyNavHost(
                navController = navController,
                startDestination = if (isAuthenticated) agendaGraphRoute else loginGraphRoute
            )
        }
    }
}