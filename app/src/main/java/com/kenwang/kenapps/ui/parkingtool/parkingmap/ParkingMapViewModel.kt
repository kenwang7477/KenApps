package com.kenwang.kenapps.ui.parkingtool.parkingmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingMapViewModel @Inject constructor(
    private val parkingListRepository: ParkingListRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<ParkingMapViewState>(
        ParkingMapViewState.Success(
            emptyList()
        )
    )
    val viewState = _viewState.asStateFlow()

    fun getParkingList() {
        viewModelScope.launch {
            _viewState.value = ParkingMapViewState.Loading
            _viewState.value = ParkingMapViewState.Success(
                parkingListRepository.getParkingList()
            )
        }
    }

    sealed class ParkingMapViewState {
        data class Success(val list: List<ParkingSpace>) : ParkingMapViewState()
        object Loading : ParkingMapViewState()
    }
}
