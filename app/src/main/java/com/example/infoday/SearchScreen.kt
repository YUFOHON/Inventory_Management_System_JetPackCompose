package com.example.infoday

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.infoday.KtorClient.search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(snackbarHostState: SnackbarHostState) {

    var searchQuery by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf(emptyList<InventoryItem>()) }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                // perform API call to retrieve search results
//                coroutineScope.launch { searchResult = search(searchQuery) }
                coroutineScope.launch {
                    // a text that show please wait for second while the search is processing
                    //the text should be at the center of screen
                    snackbarHostState.showSnackbar(
                        "start processing."
                    )
                    searchResult = search(searchQuery)
                }
//                searchResult = performSearch(searchQuery)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Search")
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(searchResult) { result ->
                when (result.type) {
                    "book" -> Book(result, coroutineScope, snackbarHostState)
                    "game" -> Game(result, coroutineScope, snackbarHostState)
                    "gift" -> Gift(result, coroutineScope, snackbarHostState)
                    "material" -> Material(result, coroutineScope, snackbarHostState)
                    else -> Text("Unknown type: ${result.type}")
                }

            }
        }
    }
}

@Composable
fun Book(
    result: InventoryItem,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
//convert the result to the correct type based on the type field
    val result = result as Book
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = result.title, fontWeight = FontWeight.Bold)
        Text(text = result.description)
        Text(("Borrower: " + result.borrower), modifier = Modifier.padding(18.dp))
        if (result.borrower.isEmpty())
            //IconButton should be place at the center of the screen
            IconButton(onClick = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "start processing."
                    )

                    if (KtorClient.borrow(result._id))
                        snackbarHostState.showSnackbar(
                            "Borrowed successfully."
                        )
                    else
                        snackbarHostState.showSnackbar(
                            "Borrowed failed."
                        )

                }

            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }

    }
}

@Composable
fun Game(
    result: InventoryItem,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
//convert the result to the correct type based on the type field
    val result = result as Game
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = result.title, fontWeight = FontWeight.Bold)
        Text(text = result.description)
        Text(("Borrower: " + result.borrower) ?: "None")
        if (result.borrower.isEmpty())
            IconButton(onClick = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "start processing."
                    )

                    if (KtorClient.borrow(result._id))
                        snackbarHostState.showSnackbar(
                            "Borrowed successfully."
                        )
                    else
                        snackbarHostState.showSnackbar(
                            "Borrowed failed."
                        )

                }

            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
    }
}

@Composable
fun Gift(
    result: InventoryItem,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
//convert the result to the correct type based on the type field
    val result = result as Gift
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = result.title, fontWeight = FontWeight.Bold)
        Text(text = result.description)
        if (result.amount > 0)
            IconButton(onClick = { /* Handle button click */ }) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
    }
}

@Composable
fun Material(
    result: InventoryItem,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
//convert the result to the correct type based on the type field
    val result = result as Material
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = result.title, fontWeight = FontWeight.Bold)
        Text(text = result.description)
        if (result.remaining > 0)
            IconButton(onClick = { /* Handle button click */ }) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
    }
}
