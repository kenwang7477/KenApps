package com.kenwang.kenapps.ui.commonscreen

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kenwang.kenapps.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowLocationPermissionView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Text(
                    text = stringResource(id = R.string.please_enable_location_permission)
                )
                Button(
                    onClick = { permissionState.launchMultiplePermissionRequest() }
                ) {
                    Text(text = stringResource(id = R.string.enable_permission))
                }
            }
        },
        permissionsNotAvailableContent = {
            EmptyView(text = stringResource(id = R.string.unknown_error))
        }
    ) {
        content.invoke()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissionView(
    modifier: Modifier = Modifier,
    permissions: List<String>,
    content: @Composable () -> Unit = {}
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions
    )

    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Text(
                    text = stringResource(id = R.string.please_enable_permissions)
                )
                Button(
                    onClick = { permissionState.launchMultiplePermissionRequest() }
                ) {
                    Text(text = stringResource(id = R.string.enable_permission))
                }
            }
        },
        permissionsNotAvailableContent = {
            EmptyView(text = stringResource(id = R.string.unknown_error))
        }
    ) {
        content.invoke()
    }
}

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.no_data)
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)
    }
}

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.no_data)
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    confirmButton: @Composable () -> Unit = {},
    cancelButton: @Composable () -> Unit = {},
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier.width(IntrinsicSize.Max),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
            ) {
                title()
                Spacer(modifier = Modifier.height(24.dp))
                content()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    cancelButton()
                    confirmButton()
                }
            }
        }
    }
}
