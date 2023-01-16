package com.kenwang.kenapps.ui.parkingtool.parkingmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.domain.usecase.parkinglist.GetParkingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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

    init {
        getParkingList()
    }

    fun getParkingList() {
        viewModelScope.launch {
            _viewState.emit(ParkingMapViewState.Loading)

            getParkingListUseCase.get().invoke().collect { result ->
                when (result) {
                    is GetParkingListUseCase.Result.Success -> {
                        _viewState.emit(
                            ParkingMapViewState.Success(result.list.toImmutableList())
                        )
                    }
                    is GetParkingListUseCase.Result.Error,
                    is GetParkingListUseCase.Result.Empty -> Unit
                }
            }
        }
    }

    sealed class ParkingMapViewState {
        data class Success(val list: ImmutableList<ParkingSpace>) : ParkingMapViewState()
        object Loading : ParkingMapViewState()
    }
}
