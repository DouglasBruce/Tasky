package com.douglasbruce.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.features.login.LoginScreen

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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(onLoginClick = { /*TODO*/ }, onSignUpClick = { /*TODO*/ })
                }
            }
        }
    }
}