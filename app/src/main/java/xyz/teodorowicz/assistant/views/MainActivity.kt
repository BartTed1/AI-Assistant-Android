package xyz.teodorowicz.assistant.views

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.teodorowicz.assistant.composables.AssistantRespondCard
import xyz.teodorowicz.assistant.composables.BottomBar
import xyz.teodorowicz.assistant.composables.TopBar
import xyz.teodorowicz.assistant.composables.UserRespondCard
import xyz.teodorowicz.assistant.ui.theme.Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(TRANSPARENT, TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(TRANSPARENT, TRANSPARENT)
        )
        setContent {
            View()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    @Preview
    fun View (modifier: Modifier = Modifier) {
        // val chat = ChatService()
        Theme {
            Scaffold(
                topBar = { TopBar() },
                bottomBar = { BottomBar() }
            ) { innerPadding ->
                Surface(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistantRespondCard(
                            text = "hdufshuu dgsdg dfhusu bguiodoi nodsdof nofso"
                        )
                        UserRespondCard()
                    }
                }
            }
        }
    }
}

