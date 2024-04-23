package xyz.teodorowicz.assistant.views

import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import xyz.teodorowicz.assistant.services.AuthService
import xyz.teodorowicz.assistant.services.SharedPreferencesService
import xyz.teodorowicz.assistant.services.UserService
import xyz.teodorowicz.assistant.ui.theme.Theme

class ConfigureActivity : ComponentActivity() {
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun View() {
        val isLoaded = remember { mutableStateOf(false) }
        val aboutUser = remember { mutableStateOf("") }
        val userLocation = remember { mutableStateOf("") }
        val isDuringSaving = remember { mutableStateOf(false) }

        LaunchedEffect(key1 = Unit) {
            val userSettings = userService.getUserSettings()
            if (userSettings != null) {
                aboutUser.value = userSettings.aboutUser ?: ""
                userLocation.value = userSettings.userLocation ?: ""
            }
            isLoaded.value = true
        }

        fun goToMainActivity() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        fun saveUserSettings() {
            if (isDuringSaving.value) return
            isDuringSaving.value = true
            lifecycleScope.launch {
                val response = userService.saveUserSettings(aboutUser.value, userLocation.value)
                isDuringSaving.value = false
                if (response.success) goToMainActivity()
                else {
                    when (response.status) {
                        401 -> goToLoginActivity()
                        500 -> Toast.makeText(this@ConfigureActivity, "Wystąpił błąd serwera", Toast.LENGTH_SHORT).show()
                        503 -> Toast.makeText(this@ConfigureActivity, "Sieć nie jest dostępna", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(this@ConfigureActivity, "Wystąpił nieznany błąd", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        Theme(
            isStatusBarDark = !isSystemInDarkTheme(),
        ) {
            Scaffold(
                bottomBar = {
                    if (isLoaded.value) {
                        BottomAppBar (
                            modifier = Modifier.imePadding(),
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            Button(
                                modifier = Modifier
                                    .height(100.dp)
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                onClick = { saveUserSettings() },
                                enabled = !isDuringSaving.value
                            ) {
                                Text(text = if (aboutUser.value.isEmpty() || userLocation.value.isEmpty()) "Pomiń" else "Zapisz")
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    if (isLoaded.value) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Witaj ${sharedPreferencesService.getString("userFirstName")}!",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Co twój asystent powinien o tobie wiedzieć?",
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(32.dp)
                                    )
                                    .fillMaxWidth(),
                                value = aboutUser.value ?: "",
                                onValueChange = { aboutUser.value = it },
                                label = { Text(text = "Coś o tobie") },
                                shape = RoundedCornerShape(8.dp),
                                maxLines = Int.MAX_VALUE,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(32.dp)
                                    )
                                    .fillMaxWidth(),
                                value = userLocation.value ?: "",
                                onValueChange = { userLocation.value = it },
                                label = { Text(text = "Przybliżona lokalizacja, np. miasto") },
                                shape = RoundedCornerShape(8.dp),
                                maxLines = Int.MAX_VALUE,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { saveUserSettings() }
                                )
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 4.dp,
                                strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
                            )
                            Spacer(modifier = Modifier.heightIn(16.dp))
                            Text(
                                text = "Ładowanie, proszę czekaj",
                                style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}