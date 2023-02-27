package com.example.infoday

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.infoday.ui.theme.InfoDayTheme

data class Dept(val name: String, val id: String) {
    companion object {
        val data = listOf(
            Dept("Computer Science", "COMP"),
            Dept("Communication Studies", "COMS")
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeptScreen() {
    LazyColumn {

        items(Dept.data) {dept->
            ListItem(
                headlineText = { Text(dept.name) },
                leadingContent = {
                    Icon(
                        Icons.Filled.ThumbUp,
                        contentDescription = null
                    )
                }
            )
            Divider()

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DeptPreview() {
    InfoDayTheme {
        DeptScreen()
    }
}
