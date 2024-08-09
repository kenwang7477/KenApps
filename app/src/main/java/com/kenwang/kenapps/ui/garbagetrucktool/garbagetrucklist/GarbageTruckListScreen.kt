package com.kenwang.kenapps.ui.garbagetrucktool.garbagetrucklist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.ui.commonscreen.EmptyView
import com.kenwang.kenapps.ui.commonscreen.ErrorView
import com.kenwang.kenapps.ui.commonscreen.LoadingView
import com.kenwang.kenapps.ui.commonscreen.ShowLocationPermissionView
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalLayoutApi::class)
object GarbageTruckListScreen {

    @Composable
    fun GarbageTruckListUI(
        paddingValues: PaddingValues,
        toGarbageTruckMap: (garbageTruck: GarbageTruck) -> Unit,
        viewModel: GarbageTruckListViewModel = hiltViewModel()
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
                    viewModel.getTrucks(getCurrentLocation(context))
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
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (networkState) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var searchText by remember {
                            mutableStateOf("")
                        }

                        val (editView, listView) = createRefs()

                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                            },
                            modifier = Modifier
                                .constrainAs(editView) {
                                    start.linkTo(anchor = parent.start, margin = 10.dp)
                                    end.linkTo(anchor = parent.end, margin = 10.dp)
                                    top.linkTo(parent.top, margin = 10.dp)
                                    width = Dimension.fillToConstraints
                                },
                            label = { Text(text = stringResource(R.string.please_input_location)) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { viewModel.search(searchText) }),
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            searchText = ""
                                            viewModel.search("")
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Clear,
                                            contentDescription = "clear"
                                        )
                                    }
                                }
                            }
                        )
                        when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                            is GarbageTruckListViewModel.GarbageTruckViewState.Success -> {
                                TruckList(
                                    modifier = Modifier
                                        .constrainAs(listView) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            top.linkTo(anchor = editView.bottom, margin = 10.dp)
                                            bottom.linkTo(parent.bottom)
                                            height = Dimension.fillToConstraints
                                        }
                                        .imeNestedScroll(),
                                    toGarbageTruckMap = toGarbageTruckMap,
                                    garbageTrucks = state.garbageTrucks
                                )
                            }
                            is GarbageTruckListViewModel.GarbageTruckViewState.Error -> {
                                ErrorView(
                                    modifier = Modifier.fillMaxSize(),
                                    text = state.errorMessage
                                )
                            }
                            is GarbageTruckListViewModel.GarbageTruckViewState.Empty -> {
                                EmptyView(
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            is GarbageTruckListViewModel.GarbageTruckViewState.Loading -> {
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
    fun TruckList(
        modifier: Modifier,
        toGarbageTruckMap: (garbageTruck: GarbageTruck) -> Unit,
        garbageTrucks: ImmutableList<GarbageTruck>
    ) {
        LazyColumn(modifier = modifier) {
            items(garbageTrucks) { truck ->
                TruckViewItem(garbageTruck = truck) {
                    toGarbageTruckMap(truck)
                }
            }
        }
    }

    @Composable
    fun TruckViewItem(garbageTruck: GarbageTruck, onClick: () -> Unit) {
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
                Text(text = garbageTruck.car)
                Text(text = garbageTruck.time)
                Text(text = garbageTruck.location)
            }
        }
    }
}
