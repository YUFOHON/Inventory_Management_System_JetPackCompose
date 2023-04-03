package com.example.infoday

//import androidx.compose.ui.focus.focusOrder
//import androidx.navigation.compose.rememberNavController
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.infoday.KtorClient.login
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.launch

@SuppressLint("DiscouragedApi")
@Composable
private fun getVideoUri(): Uri {
    val context: Context = LocalContext.current
    val resources: Resources = context.resources
    val packageName = "com.example.infoday"
    val rawId = resources.getIdentifier("clouds", "raw", packageName)
    val videoUri = Uri.parse("android.resource://$packageName/$rawId")
    return Uri.parse(videoUri.toString())
}

private fun Context.buildExoPlayer(uri: Uri) =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }

private fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    StyledPlayerView(this).apply {
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        useController = false
        resizeMode = RESIZE_MODE_ZOOM
    }

@Composable
fun LoginNav(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val startDestination = if (KtorClient.token.isEmpty()) {
        "login"
    } else {
        "profile"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable("profile") {

            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen() {
//show text "you successfully logged in" at the middle of screen
    val context: Context = LocalContext.current
    val videoUri = getVideoUri()
    val exoPlayer = remember { context.buildExoPlayer(videoUri) }
    DisposableEffect(
        AndroidView(
            factory = { it.buildPlayerView(exoPlayer) },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Transparent) // Add a transparent background color
                .zIndex(-999f)
//                .absoluteOffset(x = 0.dp, y = 300.dp)
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "You successfully logged in",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val passwordFocusRequester = FocusRequester()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context: Context = LocalContext.current
    val videoUri = getVideoUri()
    val exoPlayer = remember { context.buildExoPlayer(videoUri) }
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    DisposableEffect(
        AndroidView(
            factory = { it.buildPlayerView(exoPlayer) },
            modifier = Modifier
                .fillMaxWidth()
                .height(690.dp)
                .background(Color.Transparent) // Add a transparent background color
                .zIndex(-999f)
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }

    ProvideWindowInsets {
        Column(
            Modifier
                .padding(40.dp)
                .fillMaxSize()
                .zIndex(999f)
                .absoluteOffset(y = (-0).dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInput(InputType.Email, keybordAction = KeyboardActions(onNext = {
                passwordFocusRequester.requestFocus()
            }), value = emailValue,
                onValueChange = {
                    emailValue = it
                })
            TextInput(InputType.Password, keybordAction = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }), foucusRequester = passwordFocusRequester, value = passwordValue,
                onValueChange = {
                    passwordValue = it
                })
            Button(onClick = {
                coroutineScope.launch {

                    if (login(emailValue, passwordValue)) {
                        //go to the feed screen
                        navController.navigate("profile")
                    } else {
                        //show error message
                        snackbarHostState.showSnackbar("Invalid email or password")
                    }

                }

            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Login", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
//            Divider(
//                color = Color.Black.copy(alpha = 0.5f),
//                thickness = 1.dp,
////                modifier = Modifier.padding(top = 10.dp)
//            )
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(text = "Don't have an account? ", fontSize = 16.sp)
//                Text(text = "Sign Up", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    inputType: InputType,
    foucusRequester: FocusRequester? = null,
    keybordAction: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit
) {
//    var value by remember { mutableStateOf("") }
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(foucusRequester ?: FocusRequester()),
        leadingIcon = { Icon(imageVector = inputType.icon, contentDescription = inputType.label) },
        label = { Text(text = inputType.label) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        keyboardActions = keybordAction,
        visualTransformation = inputType.visualTransformation

    )

}

sealed class InputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object Email : InputType(
        label = "Email",
        icon = Icons.Filled.Email,
        keyboardOptions = KeyboardOptions.Default,
        visualTransformation = VisualTransformation.None
    )

    object Password : InputType(
        label = "Password",
        icon = Icons.Filled.Lock,
        keyboardOptions = KeyboardOptions.Default,
        visualTransformation = PasswordVisualTransformation()
    )
}

//@Composable
//@Preview(showBackground = true)
//fun LoginPreview() {
//    InfoDayTheme(darkTheme = isSystemInDarkTheme()) {
//        LoginScreen()
//    }
//}