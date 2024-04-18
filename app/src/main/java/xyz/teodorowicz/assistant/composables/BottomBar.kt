package xyz.teodorowicz.assistant.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.teodorowicz.assistant.ui.theme.Theme

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar() {
    val queryText = remember { mutableStateOf("") }

    fun onQueryTextChange(newText: String) {
        queryText.value = newText
    }

    Theme {
        BottomAppBar(
            modifier = Modifier
                .height(150.dp),
            containerColor = Color.Transparent,
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                TextField(
                    modifier = Modifier
                        .weight(5f)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(32.dp)),
                    value = queryText.value,
                    onValueChange = { onQueryTextChange(it) },
                    shape = RoundedCornerShape(8.dp),
                    maxLines = Int.MAX_VALUE,
                    placeholder = { Text(text = "Zadaj pytanie") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                Button(
                    modifier = Modifier
                        .width(56.dp)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = ">")
                }
            }
        }
    }
}