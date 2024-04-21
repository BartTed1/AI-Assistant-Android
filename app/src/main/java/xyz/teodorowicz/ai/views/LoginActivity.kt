package xyz.teodorowicz.ai.views

import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import xyz.teodorowicz.ai.ui.theme.Theme
import xyz.teodorowicz.ai.services.AuthService
import xyz.teodorowicz.ai.ui.theme.GoogleColor

class LoginActivity : ComponentActivity() {
    lateinit var auth: AuthService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = AuthService(this)
        if (auth.isUserLoggedIn()) goToMainActivity()
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

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginWithGoogle() {
        auth.loginWithGoogle()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun View() {
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
                        Text(
                            text = "Zaloguj się",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(
                            text = "Bądź zarejestruj się, aby korzystać z aplikacji",
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { loginWithGoogle() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoogleColor,
                                contentColor = Color(0xFFFFFFFF)
                            )
                        ) {
                            Text(modifier = Modifier.padding(16.dp), text = "Zaloguj się przez Google")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground,
                                contentColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Text(modifier = Modifier.padding(16.dp), text = "Zaloguj się przez mail")
                        }
                    }
                }
            }
        }
    }
}