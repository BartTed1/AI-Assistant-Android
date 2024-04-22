package xyz.teodorowicz.assistant.views

import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import xyz.teodorowicz.assistant.services.AuthService
import xyz.teodorowicz.assistant.services.SharedPreferencesService
import xyz.teodorowicz.assistant.services.UserService
import xyz.teodorowicz.assistant.ui.theme.GoogleColor
import xyz.teodorowicz.assistant.ui.theme.Theme

class ConfigureView : ComponentActivity() {
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var userService: UserService
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferencesService = SharedPreferencesService(this)
        userService = UserService(sharedPreferencesService.getUser(), this)
        authService = AuthService(this)
        super.onCreate(savedInstanceState)
        if (!authService.isUserLoggedIn()) goToLoginActivity()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                TRANSPARENT,
                TRANSPARENT
            )
        )

        setContent {
            View()
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Composable
    fun View() {

        LaunchedEffect(key1 = Unit) {
            val userSettings = userService.getUserSettings()
            
        }

        Theme(
            isStatusBarDark = !isSystemInDarkTheme(),
        ) {
            Scaffold { innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {

                    }
                }
            }
        }
    }
}