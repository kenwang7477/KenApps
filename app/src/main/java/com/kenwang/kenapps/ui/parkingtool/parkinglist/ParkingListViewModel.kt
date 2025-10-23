package com.kenwang.kenapps.ui.parkingtool.parkinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
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
class ParkingListViewModel @Inject constructor(
    private val getParkingListUseCase: Provider<GetParkingListUseCase>
) : ViewModel() {
    private val _viewState = MutableStateFlow<ParkingListViewState>(ParkingListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    fun getParkingList(currentLatLng: LatLng? = null, selectedCity: ParkingSpaceCity? = null) {
        viewModelScope.launch {
            val city = selectedCity ?: ParkingSpaceCity.entries.first()

            _viewState.emit(ParkingListViewState.Loading)

            getParkingListUseCase.get()
                .invoke(currentLatLng = currentLatLng, city = city)
                .collect { result ->
                    when (result) {
                        is GetParkingListUseCase.Result.Empty -> {
                            _viewState.emit(ParkingListViewState.Empty)
                        }

                        is GetParkingListUseCase.Result.Success -> {
                            _viewState.emit(
                                ParkingListViewState.Success(result.list.toList())
                            )
                        }
                        is GetParkingListUseCase.Result.Error -> {
                            _viewState.emit(
                                ParkingListViewState.Error(result.exception.errorMessage)
                            )
                        }
                    }
                }
        }
    }

    sealed class ParkingListViewState {
        data class Success(val list: List<ParkingSpace>) : ParkingListViewState()
        data class Error(val errorMessage: String) : ParkingListViewState()
        data object Empty : ParkingListViewState()
        data object Loading : ParkingListViewState()
    }
}
