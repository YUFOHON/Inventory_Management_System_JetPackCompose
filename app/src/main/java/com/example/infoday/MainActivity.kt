package com.example.infoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.infoday.ui.theme.InfoDayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.infoday.KtorClient.getFeeds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoDayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting("Android")
                    ScaffoldScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
Scaffold(
topBar = {
    fun ScaffoldScreen() {
        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf("Home", "Events", "Itin", "Map", "Info")
        val navController = rememberNavController()
        val feeds = produceState(
            initialValue = listOf<Feed>(),
            producer = {
                value = getFeeds()
            }
        )

        TopAppBar(
            title = { Text("HKBU InfoDay App") },
            navigationIcon = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                if (navBackStackEntry?.arguments?.getBoolean("topLevel") == false) {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                } else {
                    null
                }
            }
        )
    },
    bottomBar = {
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    },
    content = { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            when (selectedItem) {
                0 -> FeedScreen(feeds.value)

                1 -> DeptNav(navController)
                2 -> InfoScreen()
                3 -> MapScreen()
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