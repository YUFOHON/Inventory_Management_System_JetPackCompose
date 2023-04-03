package com.example.infoday

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import kotlinx.serialization.Serializable

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.infoday.ui.theme.InfoDayTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Serializable
sealed class InventoryItem() {
    abstract val type: String
}

@Serializable
data class Book(
    val _id: String,
    val title: String,
    val author: String,
    val year: String,
    val isbn: String,
    val description: String,
    val category: String,
    val publisher: String,
    val location: String,
    val image: String,
    val remark: String,
    override val type: String,
    val borrower: String
) : InventoryItem()

@Serializable
data class Game(
    val _id: String,
    val title: String,
    val image: String,
    val quantity: Int,
    val description: String,
    val category: String,
    val publisher: String,
    val location: String,
    val remark: String,
    override val type: String,
    val borrower: String
) : InventoryItem()

@Serializable
data class Gift(
    val _id: String,
    val title: String,
    val image: String,
    val donatedBy: String,
    val description: String,
    val category: String,
    val amount: Int,
    val unitPrice: Int,
    val location: String,
    val remark: String,
    override val type: String,
    val remaining: Int
) : InventoryItem()

@Serializable
data class Material(
    val _id: String,
    val title: String,
    val image: String,
    val description: String,
    val category: String,
    val quantity: Int,
    val location: String,
    val remark: String,
    override val type: String,
    val remaining: Int
) : InventoryItem()

@Composable
fun FeedNav(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(navController = navController, startDestination = "feed") {
        composable("feed") {
            FeedScreen(navController = navController)
        }

        composable("feed/book") {
            itemScreen(
                navController = navController,
                type = "book",
                snackbarHostState = snackbarHostState
            )
        }
        composable("feed/game") {
            itemScreen(
                navController = navController,
                type = "game",
                snackbarHostState = snackbarHostState
            )
        }
        composable("feed/gift") {
            itemScreen(
                navController = navController,
                type = "gift",
                snackbarHostState = snackbarHostState
            )
        }
        composable("feed/material") {
            itemScreen(
                navController = navController,
                type = "material",
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
fun CategoryButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    Column(
        Modifier
            .width(100.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            Modifier
                .size(100.dp)
                .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
        ) {
            Image(painter = icon, contentDescription = null, modifier = Modifier.padding(10.dp))
        }
        Text(text = text, modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold)
    }


}

@Composable
fun PopularItem(
    title: String,
    num: String,
    imagePainter: Painter
) {
    Card(Modifier.width(160.dp)) {
        Column(Modifier.padding(horizontal = 8.dp)) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )
            Text(text = title, fontWeight = FontWeight.Bold)
            Row {
                Text(text = "Number: ", fontWeight = FontWeight.Bold)
                Text(text = num)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navController: NavHostController) {
    Column() {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CategoryButton(
                text = "Game",
                icon = painterResource(id = R.drawable.game),
                onClick = { /*TODO*/ navController.navigate("feed/game") },
                backgroundColor = Color(0xFFE6B0AA)
            )
            CategoryButton(
                text = "Book",
                icon = painterResource(id = R.drawable.hkbu_logo),
                onClick = { navController.navigate("feed/book") },
                backgroundColor = Color(0xFFD7BDE2)
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CategoryButton(
                text = "Gift",
                icon = painterResource(id = R.drawable.gift),
                onClick = { navController.navigate("feed/gift") },
                backgroundColor = Color(0xFFAED6F1)
            )
            CategoryButton(
                text = "Material",
                icon = painterResource(id = R.drawable.hkbu_logo),
                onClick = { navController.navigate("feed/material") },
                backgroundColor = Color(0xFFA9DFBF)
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            // create a text that says popular
            Text(
                text = "Popular Items",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)

            )
        }
        LazyRow(
            Modifier.height(190.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            // add some to the bottom to display something make ui look better, because for now lower part is bit empty
            item {
                PopularItem(
                    title = "chess",
                    num = "10",
                    imagePainter = painterResource(id = R.drawable.ic_cheese)
                )
            }
            item {
                PopularItem(
                    title = "coin",
                    num = "10",
                    imagePainter = painterResource(id = R.drawable.ic_coin)
                )
            }
            item {
                PopularItem(
                    title = "Meat",
                    num = "10",
                    imagePainter = painterResource(id = R.drawable.ic_meat)
                )
            }
            item {
                PopularItem(
                    title = "Orange",
                    num = "10",
                    imagePainter = painterResource(id = R.drawable.ic_orange)
                )
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun FeedPreview() {
    InfoDayTheme(darkTheme = isSystemInDarkTheme()) {
        val navController = rememberNavController()
        FeedScreen(navController)
    }
}