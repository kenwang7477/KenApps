package com.kenwang.kenapps.ui.maplocation.maplocationmap

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kenwang.kenapps.data.model.MapLocation
import com.kenwang.kenapps.domain.usecase.maplocation.GetMapLocationListUseCase
import com.kenwang.kenapps.domain.usecase.maplocation.InsertMapLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class MapLocationMapViewModel @Inject constructor(
    private val getMapLocationListUseCase: Provider<GetMapLocationListUseCase>,
    private val insertMapLocationUseCase: Provider<InsertMapLocationUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<MapLocationMapViewState>(MapLocationMapViewState.Empty)
    val viewState = _viewState.asStateFlow()

    init {
        getMapLocationList()
    }

    fun addMapLocation(title: String, description: String, latLng: LatLng, uri: Uri?) {
        viewModelScope.launch {
            val mapLocation = MapLocation(
                timestamp = System.currentTimeMillis(),
                title = title,
                description = description,
                longitude = latLng.longitude,
                latitude = latLng.latitude,
                pictureUri = uri
            )
            insertMapLocationUseCase.get().invoke(mapLocation)
        }
    }

    private fun getMapLocationList() {
        viewModelScope.launch {
            getMapLocationListUseCase.get().invoke().collect { result ->
                when (result) {
                    is GetMapLocationListUseCase.Result.Success -> {
                        _viewState.emit(MapLocationMapViewState.Success(result.list.toList()))
                    }
                    is GetMapLocationListUseCase.Result.Empty -> {
                        _viewState.emit(MapLocationMapViewState.Empty)
                    }
                }
            }
        }
    }

    sealed class MapLocationMapViewState {
        data class Success(val list: List<MapLocation>) : MapLocationMapViewState()
        object Empty : MapLocationMapViewState()
    }
}
