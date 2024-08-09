package com.kenwang.kenapps.ui.maplocation.maplocationmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
import com.kenwang.kenapps.ui.commonscreen.ShowPermissionView
import com.kenwang.kenapps.utils.FileUtil
import com.kenwang.kenapps.utils.TimeUtil
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

object MapLocationMapScreen {

    @Composable
    fun MapLocationMapUI(
        paddingValues: PaddingValues,
        targetLongitude: Double?,
        targetLatitude: Double?,
        viewModel: MapLocationMapViewModel = hiltViewModel()
    ) {
        ShowPermissionView(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            )
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
                    val coroutineScope = rememberCoroutineScope()
                    AddLocationDialog(
                        onConfirm = { title, description, uri ->
                            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            val latLng = getCurrentLocation(context, locationManager)
                            coroutineScope.launch {
//                                val uri = bitmap?.let {
//                                    FileUtil.saveImageToAppFolder(context, it)
//                                }
                                viewModel.addMapLocation(title, description, latLng, uri)
                                openDialog.value = false
                            }
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
        onConfirm: (title: String, description: String, pictureUri: Uri?) -> Unit,
        onDismiss: () -> Unit
    ) {
        var titleText by remember { mutableStateOf("") }
        var descText by remember { mutableStateOf("") }
        val context = LocalContext.current
        val fileName = TimeUtil.timestampToDate(
            System.currentTimeMillis(),
            "yyyyMMddHHmm"
        )
        val pictureUri = FileUtil.getAppFolderImageUri(context, fileName)
        var pictureUriState by remember { mutableStateOf<Uri?>(null) }

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
                        onConfirm(titleText, descText, pictureUriState)
                    }
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            cancelButton = {
                TextButton(
                    onClick = {
                        FileUtil.deleteFileFromUri(context, pictureUri)
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            onDismiss = {
                FileUtil.deleteFileFromUri(context, pictureUri)
                onDismiss()
            }
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (titleId, titleFieldId, descId, descFieldId, takePictureButtonId, pictureImageId, deletePictureButtonId) = createRefs()
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
                    modifier = Modifier
                        .constrainAs(titleFieldId) {
                            top.linkTo(parent.top)
                            start.linkTo(anchor = barrier, margin = 10.dp)
//                        width = Dimension.fillToConstraints
                        }
                        .width(200.dp)
                )
                OutlinedTextField(
                    value = descText,
                    onValueChange = { descText = it },
                    modifier = Modifier
                        .constrainAs(descFieldId) {
                            top.linkTo(anchor = titleFieldId.bottom, margin = 20.dp)
                            start.linkTo(anchor = barrier, margin = 10.dp)
//                        width = Dimension.fillToConstraints
                        }
                        .width(200.dp)
                )

                var result by remember { mutableStateOf(false) }
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                    result = it
                }

                if (result) {
                    pictureUriState = pictureUri
                    Button(
                        modifier = Modifier.constrainAs(deletePictureButtonId) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(anchor = descFieldId.bottom, margin = 10.dp)
                        },
                        onClick = {
                            FileUtil.deleteFileFromUri(context, pictureUri)
                            result = false
                            pictureUriState = null
                        }
                    ) {
                        Text(text = stringResource(id = R.string.delete_picture))
                    }
                    AsyncImage(
                        modifier = Modifier
                            .constrainAs(pictureImageId) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(anchor = deletePictureButtonId.bottom, margin = 10.dp)
                            }
                            .sizeIn(maxWidth = 300.dp, maxHeight = 300.dp),
                        model = ImageRequest.Builder(context)
                            .data(pictureUri)
                            .build(),
                        contentDescription = null
                    )
                } else {
                    Button(
                        modifier = Modifier.constrainAs(takePictureButtonId) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(anchor = descFieldId.bottom, margin = 10.dp)
                        },
                        onClick = { launcher.launch(pictureUri) }
                    ) {
                        Text(text = stringResource(id = R.string.take_picture))
                    }
                }
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
