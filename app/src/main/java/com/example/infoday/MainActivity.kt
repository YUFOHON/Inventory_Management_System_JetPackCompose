package com.example.infoday

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import com.example.infoday.ui.theme.InfoDayTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoDayTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Events", "Itin", "Map", "Info")


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HKBU InfoDay App") }
            )
        },
        bottomBar = { NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }},
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
            ) {
                when (selectedItem) {
                    0 -> InfoScreen()
                    1 -> InfoScreen()
                    2 -> InfoScreen()
                    3 -> InfoScreen()
                    4 -> InfoScreen()
                }
            }
        }
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InfoDayTheme {
        ScaffoldScreen()
    }
}