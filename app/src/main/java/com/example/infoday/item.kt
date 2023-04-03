package com.example.infoday

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.infoday.KtorClient.borrow
import com.example.infoday.KtorClient.consume
import com.example.infoday.KtorClient.firstname
import com.example.infoday.KtorClient.getBook
//import com.example.infoday.KtorClient.getFeeds
import com.example.infoday.KtorClient.getGame
import com.example.infoday.KtorClient.getGifts
import com.example.infoday.KtorClient.getMaterials
import com.example.infoday.KtorClient.lastname
import com.example.infoday.KtorClient.returnItem
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun itemScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    type: String
) {
    val coroutineScope = rememberCoroutineScope()
    var page = remember { mutableStateOf(1) }
//    Log.d("itemScreen", "type: $type")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ShowLoadingMessage()
    }
    val itemsList = produceState(
        key1 = page.value,
        initialValue = when (type) {
            "game" -> listOf<Game>()
            "book" -> listOf<Book>()
            "gift" -> listOf<Gift>()
            "material" -> listOf<Material>()
            else -> listOf<Any>()
        }, producer = {
            value = when (type) {
                "game" ->value + getGame(page.value)
                "book" -> value + getBook(page.value)
                "gift" -> value + getGifts(page.value)
                "material" -> value + getMaterials(page.value)

                else -> listOf<Any>()
            }
//            hideLoadingMessage()
        }
    )

    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        items(itemsList.value) { item ->
            val itemID = when (item) {
                is Game -> item._id
                is Book -> item._id
                is Gift -> item._id
                is Material -> item._id
                else -> null
            }
            val title = when (item) {
                is Book -> item.title
                is Game -> item.title
                is Gift -> item.title
                is Material -> item.title
                else -> "cannot check the type!"
            }
            var borrower by remember { mutableStateOf("") }
            var remainning by remember { mutableStateOf(0) }
            borrower = when (item) {
                is Book -> item.borrower
                is Game -> item.borrower
                else -> "cannot check the type!"
            }
            remainning = when (item) {
                is Gift -> item.remaining
                is Material -> item.remaining
                else -> 0
            }
            val image = when (item) {
                is Book -> item.image
                is Game -> item.image
                is Gift -> item.image
                is Material -> item.image
                else -> null
            }
            ListItem(
//                AsyncImage(model = image, contentDescription ="" )
                headlineText = { Text(title, color = Color.Blue) },
                leadingContent = {
                    if (image != null) {
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(150.dp)
                        ) {
                            AsyncImage(
                                model = image,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                },

                supportingText = {
                    if (item is Game || item is Book) Text("Borrower: " + borrower)
                    else Text("Remaining: $remainning")
                },

                trailingContent = {
//                    sbeceril0@unc.edu
                    if (item is Game || item is Book) {
                        if (borrower == "none"||borrower=="")
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        "start processing."
                                    )

                                    if (borrow(itemID as String)) {
                                        snackbarHostState.showSnackbar(
                                            "Borrowed successfully."
                                        )
                                        borrower = "me"
                                    } else
                                        snackbarHostState.showSnackbar(
                                            "Borrowed failed."
                                        )

                                }

                            }) {
                                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
                            }
                        else {
                            if (borrower == "me")
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "start processing."
                                        )

                                        if (returnItem(itemID as String)) {
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
                    } else {
                        if (remainning> 0)
                            IconButton(onClick = { coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "start processing."
                                )

                                if (consume(itemID as String)) {
                                    snackbarHostState.showSnackbar(
                                        "Consume successfully."
                                    )
                                    remainning -= 1
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

            )


        }
    }

    listState.OnBottomReached {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                "go to next page."
            )
            page.value += 1

            snackbarHostState.showSnackbar(
                "Loaded successfully."
            )
        }
    }

}

@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    // state object which tells us if we should load more
//    val shouldLoadMore = remember {
//        derivedStateOf {
//            // get last visible item
//            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
//                ?:
//                // list is empty
//                // return false here if loadMore should not be invoked if the list is empty
//                return@derivedStateOf true
//
//            // Check if last visible item is the last item in the list
//            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
//        }
//    }
    val shouldLoadMore = remember {
        derivedStateOf {

            // get last visible item if layout information is available
            val lastVisibleItem = if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
                layoutInfo.visibleItemsInfo.lastOrNull()
            } else {
                null
            } ?: return@derivedStateOf false

            // Check if last visible item is the last item in the list
            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}


@Composable
fun ShowLoadingMessage() {
    LoadingAnimation()
    Text("Loading...")
}

@Composable
fun LoadingAnimation(
    indicatorSize: Dp = 100.dp,
    circleColors: List<Color> = listOf(
        Color(0xFF5851D8),
        Color(0xFF833AB4),
        Color(0xFFC13584),
        Color(0xFFE1306C),
        Color(0xFFFD1D1D),
        Color(0xFFF56040),
        Color(0xFFF77737),
        Color(0xFFFCAF45),
        Color(0xFFFFDC80),
        Color(0xFF5851D8)
    ),
    animationDuration: Int = 360

) {

    val infiniteTransition = rememberInfiniteTransition()

    val rotateAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            )
        )
    )

    CircularProgressIndicator(
        modifier = Modifier
            .size(size = indicatorSize)
            .rotate(degrees = rotateAnimation)
            .border(
                width = 4.dp,
                brush = Brush.sweepGradient(circleColors),
                shape = CircleShape
            ),
        progress = 1f,
        strokeWidth = 1.dp,
        color = MaterialTheme.colorScheme.primary // Set background color
    )
}