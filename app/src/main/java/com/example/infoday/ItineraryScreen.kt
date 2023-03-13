package com.example.infoday

import android.app.Application
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.infoday.ui.theme.InfoDayTheme
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItinerarytScreen(snackbarHostState: SnackbarHostState) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val eventViewModel: EventViewModel = viewModel(
        factory = EventViewModelFactory(context.applicationContext as Application)
    )

    val events by eventViewModel.readAllSavedData.observeAsState(listOf())
    Log.d("events", events.toString())
//    for (event in events) {
//        Log.d("events", event.title)
//    }
    LazyColumn {
        items(events) { event ->
            ListItem(
                headlineText = { Text(event.title) },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "Event has been removed."
                                )
                            }
//                            eventViewModel.bookmarkEvent(event)
                            eventViewModel.removeEvent(event)
                        }
                    )
                }
            )
            Divider()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ItineraryPreview() {
    InfoDayTheme(darkTheme = isSystemInDarkTheme()) {
//        ItineraryScreen()
    }
}