package com.kenwang.kenapps.ui.parkingtool.parkingmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceCity
import com.kenwang.kenapps.extensions.cleanLineBreak
import com.kenwang.kenapps.ui.theme.Blue90

object ParkingMapScreen {

    @Composable
    fun ParkingMapUI(
        paddingValues: PaddingValues,
        parkingSpace: ParkingSpace,
        city: ParkingSpaceCity,
        parkingMapViewModel: ParkingMapViewModel = hiltViewModel()
    ) {
        LaunchedEffect(Unit) {
            parkingMapViewModel.getParkingList(city = city)
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val latlng = LatLng(parkingSpace.latitude, parkingSpace.longitude)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(latlng, 15f)
            }
            val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
            val uiSettings by remember { mutableStateOf(MapUiSettings()) }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = properties
            ) {
                when (val state = parkingMapViewModel.viewState.collectAsStateWithLifecycle().value) {
                    is ParkingMapViewModel.ParkingMapViewState.Loading -> Unit
                    is ParkingMapViewModel.ParkingMapViewState.Success -> {
//                            Circle(
//                                center = latlng,
//                                radius = 500.0,
//                                fillColor = Blue90,
//                                strokeColor = Blue90
//                            )
                        state.list.forEach {
                            val parkingSpaceLatLng = LatLng(it.latitude, it.longitude)
                            Marker(
                                state = MarkerState(position = parkingSpaceLatLng),
                                title = it.name.cleanLineBreak(),
                                snippet = it.fareDescription.cleanLineBreak()
                            )
                        }
                    }
                }
            }
        }
    }
}
