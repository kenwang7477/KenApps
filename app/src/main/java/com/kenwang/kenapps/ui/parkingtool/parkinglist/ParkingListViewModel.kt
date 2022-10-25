package com.kenwang.kenapps.ui.parkingtool.parkinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingListViewModel @Inject constructor(
    private val parkingListRepository: ParkingListRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<ParkingListViewState>(
        ParkingListViewState.Success(
            emptyList()
        )
    )
    val viewState = _viewState.asStateFlow()

    fun getParkingList(currentLatLng: LatLng? = null) {
        viewModelScope.launch {
            _viewState.value = ParkingListViewState.Loading

            val parkingSpaces = currentLatLng?.let {
                parkingListRepository.getParkingList().sortedBy { parkingSpace ->
                    val to = LatLng(parkingSpace.latitude, parkingSpace.longitude)
                    SphericalUtil.computeDistanceBetween(currentLatLng, to)
                }
            } ?: parkingListRepository.getParkingList()
            _viewState.value = ParkingListViewState.Success(parkingSpaces)
        }
    }

    sealed class ParkingListViewState {
        data class Success(val list: List<ParkingSpace>) : ParkingListViewState()
        object Loading : ParkingListViewState()
    }
}
