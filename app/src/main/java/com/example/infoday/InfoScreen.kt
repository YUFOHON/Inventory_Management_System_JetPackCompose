package com.example.infoday

import android.media.Image
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Composable
//fun InfoGreeting() {
//    Column {
//        Image(
//            painter = painterResource(id = R.drawable.hkbu_logo),
//            contentDescription = stringResource(id = R.string.hkbu_logo),
//        )
//        Text(text = "Hello Android!")
//    }
//    }

@Preview(showBackground = true)
@Composable
fun InfoPreview() {
    InfoDayTheme {
        InfoScreen()
    }
}

@Composable
fun InfoGreeting() {
    val padding = 16.dp
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.size(padding))
        Image(
            painter = painterResource(id = R.drawable.hkbu_logo),
            contentDescription = stringResource(id = R.string.hkbu_logo),
            modifier = Modifier.size(240.dp)
        )
        Spacer(Modifier.size(padding))
        Text(text = "HKBU InfoDay App", fontSize = 30.sp)
    }
}

data class Contact(val office: String, val tel: String) {
    companion object {
        val data = listOf(
            Contact(office = "Admission Office", tel = "3411-2200"),
            Contact(office = "Emergencies", tel = "3411-7777"),
            Contact(office = "Health Services Center", tel = "3411-7447")
        )
    }
}
@Composable
fun InfoScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        InfoGreeting()
        PhoneList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneList() {
    Column {
        Contact.data.forEach { message ->
            ListItem(
                headlineText = { Text(message.office) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Call,
                        contentDescription = null
                    )
                },
                trailingContent = { Text(message.tel) }
            )
        }
    }
}