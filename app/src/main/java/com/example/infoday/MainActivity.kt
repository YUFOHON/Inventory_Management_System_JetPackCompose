package com.example.infoday

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.infoday.ui.theme.InfoDayTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter.State.Empty.painter

//import com.example.infoday.KtorClient.getFeeds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dataStore = UserPreferences(LocalContext.current)
            val mode by dataStore.getMode.collectAsState(initial = false)
//            print(mode)
            InfoDayTheme(darkTheme = mode ?: false) {
//            InfoDayTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//            Greeting("Android")
                    ScaffoldScreen()
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Item", "Search", "Login")
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        topBar = {
            if (selectedItem == 0 || selectedItem == 1)
                TopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Inventory",
                            fontSize = 30.sp,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            textAlign = TextAlign.Center
                        )
                    },

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
            // create a list of icons to store the bottom navigation bar icon
            val iconList = listOf(
                Icons.Filled.Home,
                Icons.Filled.Search,
                Icons.Filled.Info
            )
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(iconList[index], contentDescription = item) },
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
//                    0 -> FeedScreen()
                    0 -> FeedNav(navController, snackbarHostState)
                    1 -> SearchScreen(snackbarHostState)
                    2 -> LoginNav(navController,snackbarHostState)
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InfoDayTheme {
        ScaffoldScreen()
    }
}