package com.douglasbruce.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.navigation.TaskyNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // TODO: Tie to uiState to keep splashScreen visible until we can determine if user is authenticated or not
        splashScreen.setKeepOnScreenCondition {
            false
        }

        setContent {
            TaskyTheme {
                val navController: NavHostController = rememberNavController()
                TaskyNavHost(navController = navController)
            }
        }
    }
}