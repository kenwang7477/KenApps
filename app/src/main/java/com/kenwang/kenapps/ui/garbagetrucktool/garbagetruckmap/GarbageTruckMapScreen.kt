package com.kenwang.kenapps.ui.garbagetrucktool.garbagetruckmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.kenwang.kenapps.data.model.GarbageTruck
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
object GarbageTruckMapScreen {

    @Composable
    fun TruckMapUI(
        paddingValues: PaddingValues,
        garbageTruck: GarbageTruck,
        garbageTruckMapViewModel: GarbageTruckMapViewModel = hiltViewModel()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val (mapContent) = createRefs()
            Column(
                modifier = Modifier.constrainAs(mapContent) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
            ) {
                RefreshLayout(garbageTruckMapViewModel = garbageTruckMapViewModel)
                MapLayout(garbageTruck = garbageTruck, garbageTruckMapViewModel = garbageTruckMapViewModel)
            }
        }
    }

    @Composable
    private fun RefreshLayout(garbageTruckMapViewModel: GarbageTruckMapViewModel) {
        var refreshState by remember { mutableStateOf(false) }
        var refreshTime by remember { mutableStateOf("20") }
        ConstraintLayout(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            val (button, textField) = createRefs()
            Button(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(textField.start)
                    },
                shape = RectangleShape,
                onClick = {
                    refreshState = !refreshState
                    if (refreshState) {
                        val refreshTimeL = try {
                            val time = refreshTime.toLong()
                            if (time > 0) {
                                TimeUnit.SECONDS.toMillis(time)
                            } else {
                                TimeUnit.SECONDS.toMillis(5L)
                            }
                        } catch (e: Exception) {
                            TimeUnit.SECONDS.toMillis(20L)
                        }
                        refreshTime = TimeUnit.MILLISECONDS.toSeconds(refreshTimeL).toString()
                        garbageTruckMapViewModel.startRefresh(refreshTimeL)
                    } else {
                        garbageTruckMapViewModel.stopRefresh()
                    }
                }
            ) {
                if (refreshState) {
                    Text(text = stringResource(id = R.string.stop_refresh))
                } else {
                    Text(text = stringResource(id = R.string.start_refresh))
                }
            }

            val focusManager = LocalFocusManager.current
            OutlinedTextField(
                modifier = Modifier
                    .constrainAs(textField) {
                        top.linkTo(parent.top)
                        start.linkTo(button.end)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .width(200.dp),
                enabled = !refreshState,
                label = { Text(stringResource(id = R.string.refresh_label)) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                value = refreshTime,
                onValueChange = {
                    refreshTime = it
                }
            )
        }
    }

    @Composable
    fun MapLayout(garbageTruck: GarbageTruck, garbageTruckMapViewModel: GarbageTruckMapViewModel) {
        val latlng = LatLng(garbageTruck.latitude, garbageTruck.longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latlng, 15f)
        }
        val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
        val uiSettings by remember { mutableStateOf(MapUiSettings()) }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = properties
        ) {
            when (val state = garbageTruckMapViewModel.viewState.collectAsStateWithLifecycle().value) {
                is GarbageTruckMapViewModel.ViewState.Loading -> Unit
                is GarbageTruckMapViewModel.ViewState.Success -> {
                    state.garbageTrucks.forEach {
                        val truckLatLng = LatLng(it.latitude, it.longitude)
                        Marker(
                            state = MarkerState(position = truckLatLng),
                            title = it.car
                        )
                    }
                }
            }
        }
    }
}
