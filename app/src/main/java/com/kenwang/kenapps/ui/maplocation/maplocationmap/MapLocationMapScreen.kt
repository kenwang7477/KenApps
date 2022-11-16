package com.kenwang.kenapps.ui.maplocation.maplocationmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.MapLocation
import com.kenwang.kenapps.ui.commonscreen.CustomDialog
import com.kenwang.kenapps.ui.commonscreen.ShowLocationPermissionView
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
object MapLocationMapScreen {

    @Composable
    fun MapLocationMapUI(
        paddingValues: PaddingValues,
        targetLongitude: Double?,
        targetLatitude: Double?,
        viewModel: MapLocationMapViewModel = hiltViewModel()
    ) {
        ShowLocationPermissionView(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val targetLatLng = if (targetLongitude != null && targetLatitude != null) {
                LatLng(targetLatitude, targetLongitude)
            } else {
                null
            }
            Column(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            ) {
                val openDialog = remember { mutableStateOf(false)  }
                val context = LocalContext.current

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { openDialog.value = true }
                ) {
                    Text(text = stringResource(id = R.string.add_map_location))
                }
                if (openDialog.value) {
                    AddLocationDialog(
                        onConfirm = { title, description ->
                            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            val latLng = getCurrentLocation(context, locationManager)
                            viewModel.addMapLocation(title, description, latLng)
                            openDialog.value = false
                        },
                        onDismiss = {
                            openDialog.value = false
                        }
                    )
                }
                when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                    is MapLocationMapViewModel.MapLocationMapViewState.Success -> {
                        MapLocationMap(mapLocationList = state.list, targetLatLng = targetLatLng)
                    }
                    is MapLocationMapViewModel.MapLocationMapViewState.Empty -> {
                        MapLocationMap(
                            mapLocationList = emptyList<MapLocation>().toImmutableList(),
                            targetLatLng = targetLatLng
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun MapLocationMap(
        mapLocationList: ImmutableList<MapLocation>,
        targetLatLng: LatLng? = null
    ) {
        val context = LocalContext.current
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val latLng = targetLatLng ?: getCurrentLocation(context, locationManager)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLng, 15f)
        }
        val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
        val uiSettings by remember { mutableStateOf(MapUiSettings()) }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = properties
        ) {
            mapLocationList.forEach { mapLocation ->
                val mapLocationLatLng = LatLng(mapLocation.latitude, mapLocation.longitude)
                Marker(
                    state = MarkerState(position = mapLocationLatLng),
                    title = mapLocation.title,
                    snippet = mapLocation.description
                )
            }
        }
    }

    @Composable
    private fun AddLocationDialog(
        onConfirm: (title: String, description: String) -> Unit,
        onDismiss: () -> Unit
    ) {
        var titleText by remember { mutableStateOf("") }
        var descText by remember { mutableStateOf("") }

        CustomDialog(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.enter_map_location_dialog_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(titleText, descText)
                    }
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            cancelButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            onDismiss = onDismiss
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                    .padding(top = 24.dp)
            ) {
                val (titleId, titleFieldId, descId, descFieldId) = createRefs()
                val barrier = createEndBarrier(titleId, descId)

                Text(
                    text = stringResource(id = R.string.title),
                    modifier = Modifier.constrainAs(titleId) {
                        top.linkTo(titleFieldId.top)
                        bottom.linkTo(titleFieldId.bottom)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = stringResource(id = R.string.description),
                    modifier = Modifier.constrainAs(descId) {
                        top.linkTo(descFieldId.top)
                        bottom.linkTo(descFieldId.bottom)
                        start.linkTo(titleId.start)
                    }
                )
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    modifier = Modifier.constrainAs(titleFieldId) {
                        top.linkTo(parent.top)
                        start.linkTo(anchor = barrier, margin = 10.dp)
                        width = Dimension.fillToConstraints
                        end.linkTo(parent.end)
                    }
                )
                OutlinedTextField(
                    value = descText,
                    onValueChange = { descText = it },
                    modifier = Modifier.constrainAs(descFieldId) {
                        top.linkTo(anchor = titleFieldId.bottom, margin = 20.dp)
                        start.linkTo(anchor = barrier, margin = 10.dp)
                        width = Dimension.fillToConstraints
                        end.linkTo(parent.end)
                    }
                )
            }
        }
    }

    private fun getCurrentLocation(
        context: Context,
        locationManager: LocationManager
    ): LatLng {
        return if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0)
        } else LatLng(0.0, 0.0)
    }
}
