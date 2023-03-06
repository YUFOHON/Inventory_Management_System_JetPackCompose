package com.example.infoday

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.infoday.ui.theme.InfoDayTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

data class Building(val title:String, val latitude:Double, val longitude:Double) {
    companion object {
        val data = listOf(
            Building("AC Hall", 22.341280,114.179768),
            Building("Lam Woo International Conference Center", 22.337716, 114.182013),
            Building("Communication and Visual Arts Building", 22.334382, 114.182528)
        )
    }
}

@Composable
fun MapScreen() {
    val hkbu = LatLng(22.33787, 114.18131)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(hkbu, 17f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
//        Marker(
//            state = MarkerState(position = hkbu),
//            title = "HKBU",
//            snippet = "Marker in HKBU"
//        )
        Building.data.forEach { building ->
            Marker(
                state = MarkerState(position = LatLng(building.latitude, building.longitude)),
                title = building.title,
                snippet = "Marker in HKBU"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapPreview() {
    InfoDayTheme {
        MapScreen()
    }
}