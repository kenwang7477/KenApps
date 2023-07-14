package com.kenwang.kenapps.ui.cctvtool.cctvmap

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.kenwang.kenapps.data.model.CctvMonitor
import com.kenwang.kenapps.ui.cctvtool.cctvlist.CctvListViewModel
import com.kenwang.kenapps.ui.commonscreen.ShowLocationPermissionView
import com.kenwang.kenapps.utils.ChromeTabUtil

object CctvMapScreen {

    @Composable
    fun CctvMapUI(
        paddingValues: PaddingValues,
        cctvMonitor: CctvMonitor,
        viewModel: CctvListViewModel = hiltViewModel()
    ) {
        ShowLocationPermissionView(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            MapLayout(
                paddingValues = paddingValues,
                cctvMonitor = cctvMonitor,
                viewModel = viewModel
            )
        }
    }

    @Composable
    fun MapLayout(
        paddingValues: PaddingValues,
        cctvMonitor: CctvMonitor,
        viewModel: CctvListViewModel
    ) {
        val latlng = LatLng(cctvMonitor.latitude, cctvMonitor.longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latlng, 15f)
        }
        val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
        val uiSettings by remember { mutableStateOf(MapUiSettings()) }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = properties
        ) {
            val context = LocalContext.current
            when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                is CctvListViewModel.CctvListViewState.Loading,
                is CctvListViewModel.CctvListViewState.Error,
                is CctvListViewModel.CctvListViewState.Empty -> Unit
                is CctvListViewModel.CctvListViewState.Success -> {
                    state.cctvList.forEach { cctv ->
                        val cctvMonitorLatLng = LatLng(cctv.latitude, cctv.longitude)
                        Marker(
                            state = MarkerState(position = cctvMonitorLatLng),
                            title = cctv.roadsection,
                            onInfoWindowClick = {
                                toCctvWebPage(context, cctv)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun toCctvWebPage(context: Context, cctvMonitor: CctvMonitor) {
        cctvMonitor.cctvId.split("64000").getOrNull(1)?.let {
            val cctvWebPageUrl = "https://traffic.tbkc.gov.tw/CCTV/cctv_view_atis.jsp?cctv_id=${it}&amp;w=340&amp;h=260"
            ChromeTabUtil.openTab(context, cctvWebPageUrl)
        }
    }
}
