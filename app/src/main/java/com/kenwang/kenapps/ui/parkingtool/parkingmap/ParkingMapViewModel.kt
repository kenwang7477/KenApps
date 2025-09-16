package com.kenwang.kenapps.ui.parkingtool.parkingmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceCity
import com.kenwang.kenapps.domain.usecase.parkinglist.GetParkingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class ParkingMapViewModel @Inject constructor(
    private val getParkingListUseCase: Provider<GetParkingListUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<ParkingMapViewState>(ParkingMapViewState.Loading)
    val viewState = _viewState.asStateFlow()

//    init {
//        getParkingList()
//    }

    fun getParkingList(city: ParkingSpaceCity) {
        viewModelScope.launch {
            _viewState.emit(ParkingMapViewState.Loading)

            getParkingListUseCase.get().invoke(city = city).collect { result ->
                when (result) {
                    is GetParkingListUseCase.Result.Success -> {
                        _viewState.emit(
                            ParkingMapViewState.Success(result.list.toList())
                        )
                    }
                    is GetParkingListUseCase.Result.Error,
                    is GetParkingListUseCase.Result.Empty -> Unit
                }
            }
        }
    }

    sealed class ParkingMapViewState {
        data class Success(val list: List<ParkingSpace>) : ParkingMapViewState()
        object Loading : ParkingMapViewState()
    }
}
