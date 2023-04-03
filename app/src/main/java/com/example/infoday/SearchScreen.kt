package com.example.infoday

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
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
    var page = remember { mutableStateOf(1) }
    val updatedPage= rememberUpdatedState(page)
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
//                    page.value = 0
                    searchResult = search(searchQuery, page.value)

                }
//                searchResult = performSearch(searchQuery)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Search")
        }
        val listState = rememberLazyListState()
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
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
        listState.OnBottomReached {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    "go to next page."
                )
                page.value += 1
                searchResult += search(searchQuery, page.value)

                snackbarHostState.showSnackbar(
                    "Loaded successfully."
                )
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
    var borrower by remember { mutableStateOf(result.borrower) }
    val updatedBorrower = rememberUpdatedState(borrower)
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = result.title, fontWeight = FontWeight.Bold)
        Text(text = result.description)
        Text(("Borrower: " + updatedBorrower.value), modifier = Modifier.padding(18.dp))
        if (borrower == "none" || borrower == "")
            IconButton(onClick = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "start processing."
                    )

                    if (KtorClient.borrow(result._id)) {
                        snackbarHostState.showSnackbar(
                            "Borrowed successfully."
                        )
                        borrower = "me"
                    } else
                        snackbarHostState.showSnackbar(
                            "Borrowed failed."
                        )

                }

            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
        else {
            if (borrower == "me")
                IconButton(onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            "start processing."
                        )

                        if (KtorClient.returnItem(result._id)) {
                            snackbarHostState.showSnackbar(
                                "Return successfully."
                            )
                            borrower = ""
                        } else
                            snackbarHostState.showSnackbar(
                                "Return failed."
                            )

                    }

                }) {
                    Icon(Icons.Default.Info, contentDescription = "Return")
                }
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
    var borrower by remember { mutableStateOf(result.borrower) }
    val updatedBorrower = rememberUpdatedState(borrower)
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = result.title, fontWeight = FontWeight.Bold)
        Text(text = result.description)
        Text(("Borrower: " + updatedBorrower.value), modifier = Modifier.padding(18.dp))
        if (borrower == "none" || borrower == "")
            IconButton(onClick = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "start processing."
                    )

                    if (KtorClient.borrow(result._id)) {
                        snackbarHostState.showSnackbar(
                            "Borrowed successfully."
                        )
                        borrower = "me"
                    } else
                        snackbarHostState.showSnackbar(
                            "Borrowed failed."
                        )

                }

            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
        else {
            if (borrower == "me")
                IconButton(onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            "start processing."
                        )

                        if (KtorClient.returnItem(result._id)) {
                            snackbarHostState.showSnackbar(
                                "Return successfully."
                            )
                            borrower = ""
                        } else
                            snackbarHostState.showSnackbar(
                                "Return failed."
                            )

                    }

                }) {
                    Icon(Icons.Default.Info, contentDescription = "Return")
                }
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
    var remaining by remember { mutableStateOf(result.remaining) }
    val updatedRemaining = rememberUpdatedState(remaining)
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = result.title, fontWeight = FontWeight.Bold)
        Text(text = result.description)
        Text("Remaining: ${updatedRemaining.value}")
        if (updatedRemaining.value > 0)
            IconButton(onClick = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "start processing."
                    )
                    if (KtorClient.consume(result._id)) {
                        snackbarHostState.showSnackbar(
                            "Consume successfully."
                        )
                        remaining--
                    } else
                        snackbarHostState.showSnackbar(
                            "Consume failed."
                        )
                }
            }) {
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
    var remaining by remember { mutableStateOf(result.remaining) }
    val updatedRemaining = rememberUpdatedState(remaining)
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = result.title, fontWeight = FontWeight.Bold)
        Text(text = result.description)
        Text("Remaining: ${updatedRemaining.value}")
        if (updatedRemaining.value > 0)
            IconButton(onClick = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "start processing."
                    )
                    if (KtorClient.consume(result._id)) {
                        snackbarHostState.showSnackbar(
                            "Consume successfully."
                        )
                        remaining--
                    } else
                        snackbarHostState.showSnackbar(
                            "Consume failed."
                        )
                }
            }) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
    }
}
