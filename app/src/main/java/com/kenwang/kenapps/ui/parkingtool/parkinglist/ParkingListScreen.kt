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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceCity
import com.kenwang.kenapps.extensions.cleanLineBreak
import com.kenwang.kenapps.ui.commonscreen.EmptyView
import com.kenwang.kenapps.ui.commonscreen.ErrorView
import com.kenwang.kenapps.ui.commonscreen.LoadingView
import com.kenwang.kenapps.ui.commonscreen.ShowLocationPermissionView

object ParkingListScreen {

    @SuppressLint("PermissionLaunchedDuringComposition")
    @Composable
    fun ParkingListUI(
        paddingValues: PaddingValues,
        toParkingMap: (parkingSpace: ParkingSpace, city: ParkingSpaceCity) -> Unit,
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
            val city = rememberSaveable { mutableStateOf(ParkingSpaceCity.Taipei) }
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    networkState = true
                    viewModel.getParkingList(currentLatLng = getCurrentLocation(context), selectedCity = city.value)
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
                CitySelector(
                    viewModel = viewModel,
                    city = city.value,
                    onCitySelected = { parkingSpaceCity ->
                        city.value = parkingSpaceCity
                    }
                )
                if (networkState) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                            is ParkingListViewModel.ParkingListViewState.Success -> {
                                ParkingList(
                                    toParkingMap = toParkingMap,
                                    city = city.value,
                                    parkingSpaces = state.list
                                )
                            }
                            is ParkingListViewModel.ParkingListViewState.Error -> {
                                ErrorView(
                                    modifier = Modifier.fillMaxSize(),
                                    state.errorMessage
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
    private fun CitySelector(
        viewModel: ParkingListViewModel,
        city: ParkingSpaceCity,
        onCitySelected: (ParkingSpaceCity) -> Unit
    ) {
        val cities = getCities()
        var expanded by remember { mutableStateOf(false) }
        var selectedCity by remember { mutableStateOf(cities.find { it.enum.cityName == city.cityName }?.name ?: "") }
        var textFieldSize by remember { mutableStateOf(Size.Zero) }
        val icon = if (expanded) {
            Icons.Filled.KeyboardArrowUp
        } else {
            Icons.Filled.KeyboardArrowDown
        }

        Column(Modifier.padding(20.dp)) {
            OutlinedTextField(
                value = selectedCity,
                readOnly = true,
                enabled = false,
                onValueChange = {
                    selectedCity = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    },
                colors = TextFieldDefaults.colors(
                    disabledLabelColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.Transparent,
                    disabledIndicatorColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                ),
                label = { Text(text = stringResource(id = R.string.city)) },
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = "contentDescription"
                    )
                }
            )

            DropdownMenu(
                modifier = Modifier.width(with(LocalDensity.current){ textFieldSize.width.toDp() }),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                cities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(text = city.name) },
                        onClick = {
                            selectedCity = city.name
                            expanded = false
                            val parkingSpaceCity = ParkingSpaceCity.entries.find { city.enum == it }
                            parkingSpaceCity?.let { onCitySelected.invoke(it) }
                            viewModel.getParkingList(selectedCity = parkingSpaceCity)
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun ParkingList(
        toParkingMap: (parkingSpace: ParkingSpace, city: ParkingSpaceCity) -> Unit,
        city: ParkingSpaceCity,
        parkingSpaces: List<ParkingSpace>
    ) {
        LazyColumn {
            items(parkingSpaces) { parkingSpace ->
                ParkingViewItem(parkingSpace = parkingSpace) {
                    toParkingMap(parkingSpace, city)
                }
            }
        }
    }

    @Composable
    private fun ParkingViewItem(parkingSpace: ParkingSpace, onClick: () -> Unit) {
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .padding(5.dp)
                .clickable(onClick = onClick)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "${stringResource(id = R.string.name)}:${parkingSpace.name.cleanLineBreak()}")
                Text(text = "${stringResource(id = R.string.park_description)}:${parkingSpace.description.cleanLineBreak()}")
                Text(text = "${stringResource(id = R.string.charge)}:${parkingSpace.fareDescription.cleanLineBreak()}")
                Text(text = "${stringResource(id = R.string.address)}:${parkingSpace.address.cleanLineBreak()}")
                Text(text = "${stringResource(id = R.string.contact_information)}:${parkingSpace.emergencyPhone.cleanLineBreak()}")
            }
        }
    }

    @Composable
    private fun getCities(): List<ParkCity> {
        val cityNameMap = mapOf(
            ParkingSpaceCity.Taipei to R.string.taipei,
            ParkingSpaceCity.Taoyuan to R.string.taoyuan,
            ParkingSpaceCity.Taichung to R.string.taichung,
            ParkingSpaceCity.Tainan to R.string.tainan,
            ParkingSpaceCity.Kaohsiung to R.string.kaohsiung,
            ParkingSpaceCity.Keelung to R.string.keelung,
            ParkingSpaceCity.Hsinchu to R.string.hsinchu,
            ParkingSpaceCity.HsinchuCounty to R.string.hsinchu_county,
            ParkingSpaceCity.MiaoliCounty to R.string.miaoli_county,
            ParkingSpaceCity.ChanghuaCounty to R.string.changhua_county,
            ParkingSpaceCity.NantouCounty to R.string.nantou_county,
            ParkingSpaceCity.YunlinCounty to R.string.yunlin_county,
            ParkingSpaceCity.Chiayi to R.string.chiayi,
            ParkingSpaceCity.ChiayiCounty to R.string.chiayi_county,
            ParkingSpaceCity.PingtungCounty to R.string.pingtung_county,
            ParkingSpaceCity.YilanCounty to R.string.yilan_county,
            ParkingSpaceCity.HualienCounty to R.string.hualien_county,
            ParkingSpaceCity.TaitungCounty to R.string.taitung_county
        )

        return ParkingSpaceCity.entries.mapNotNull { city ->
            cityNameMap[city]?.let { ParkCity(name = stringResource(it), enum = city) }
        }
    }

    private data class ParkCity(
        val name: String,
        val enum: ParkingSpaceCity
    )
}
