package com.kenwang.kenapps.ui.parkingtool.parkinglist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.extensions.cleanLineBreak
import com.kenwang.kenapps.ui.commonscreen.EmptyView
import com.kenwang.kenapps.ui.commonscreen.LoadingView
import com.kenwang.kenapps.ui.commonscreen.ShowLocationPermissionView
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalLifecycleComposeApi::class)
object ParkingListScreen {

    @SuppressLint("PermissionLaunchedDuringComposition")
    @Composable
    fun ParkingListUI(
        paddingValues: PaddingValues,
        toParkingMap: (parkingSpace: ParkingSpace) -> Unit,
        viewModel: ParkingListViewModel = hiltViewModel()
    ) {
        ShowLocationPermissionView(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val context = LocalContext.current
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var networkState by remember {
                mutableStateOf(false)
            }
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    networkState = true
                    viewModel.getParkingList(getCurrentLocation(context))
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    networkState = false
                }

                fun getCurrentLocation(context: Context): LatLng? {
                    return if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val location =
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        location?.let { LatLng(it.latitude, it.longitude) }
                    } else null
                }
            }
            DisposableEffect(key1 = Unit) {
                connectivityManager.registerDefaultNetworkCallback(callback)
                onDispose { connectivityManager.unregisterNetworkCallback(callback) }
            }

            Column(
                modifier = Modifier.padding(paddingValues).fillMaxSize()
            ) {
                if (networkState) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                            is ParkingListViewModel.ParkingListViewState.Success -> {
                                ParkingList(
                                    modifier = Modifier.padding(top = 10.dp),
                                    toParkingMap = toParkingMap,
                                    parkingSpaces = state.list
                                )
                            }
                            is ParkingListViewModel.ParkingListViewState.Empty -> {
                                EmptyView(
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            is ParkingListViewModel.ParkingListViewState.Loading -> {
                                LoadingView(
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                } else {
                    EmptyView(
                        modifier = Modifier.fillMaxSize(),
                        text = stringResource(R.string.please_enable_network)
                    )
                }
            }
        }
    }

    @Composable
    fun ParkingList(
        modifier: Modifier,
        toParkingMap: (parkingSpace: ParkingSpace) -> Unit,
        parkingSpaces: ImmutableList<ParkingSpace>
    ) {
        LazyColumn(modifier = modifier) {
            items(parkingSpaces) { parkingSpace ->
                ParkingViewItem(parkingSpace = parkingSpace) {
                    toParkingMap(parkingSpace)
                }
            }
        }
    }

    @Composable
    fun ParkingViewItem(parkingSpace: ParkingSpace, onClick: () -> Unit) {
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .padding(5.dp)
                .clickable(onClick = onClick)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "地區：${parkingSpace.area.cleanLineBreak()}")
                Text(text = "類型：${parkingSpace.type.cleanLineBreak()}")
                Text(text = "名稱：${parkingSpace.name.cleanLineBreak()}")
                Text(text = "收費：${parkingSpace.charges.cleanLineBreak()}")
                Text(text = "聯絡資訊：${parkingSpace.information.cleanLineBreak()}")
            }
        }
    }
}
