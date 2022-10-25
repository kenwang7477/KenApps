package com.kenwang.kenapps.ui.garbagetrucktool.garbagetrucklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GarbageTruckListViewModel @Inject constructor(
    private val garbageTruckRepository: GarbageTruckRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<GarbageTruckViewState>(GarbageTruckViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private var garbageTruckList: List<GarbageTruck> = emptyList()
    private var filterString: String = ""

    fun getTrucks(currentLatLng: LatLng?, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _viewState.value = GarbageTruckViewState.Loading
            if (garbageTruckList.isEmpty() || forceRefresh) {

                val trucks = currentLatLng?.let {
                    garbageTruckRepository.getTrucks()
                        .sortedBy {
                            val to = LatLng(it.latitude, it.longitude)
                            SphericalUtil.computeDistanceBetween(currentLatLng, to)
                        }
                } ?: garbageTruckRepository.getTrucks()
                _viewState.emit(GarbageTruckViewState.Success(trucks))
                garbageTruckList = trucks
            } else {
                _viewState.emit(GarbageTruckViewState.Success(garbageTruckList))
            }
        }
    }

    fun search(title: String = "") {
        filterString = title
        val trucks = if (title.isNotBlank()) {
            garbageTruckList.filter { it.location.contains(title, true) }
        } else {
            garbageTruckList
        }
        viewModelScope.launch {
            _viewState.emit(GarbageTruckViewState.Success(trucks))
        }
    }

    sealed class GarbageTruckViewState {
        object Loading : GarbageTruckViewState()
        data class Success(val garbageTrucks: List<GarbageTruck>) : GarbageTruckViewState()
    }
}
