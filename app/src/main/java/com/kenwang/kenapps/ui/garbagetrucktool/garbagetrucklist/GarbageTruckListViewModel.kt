package com.kenwang.kenapps.ui.garbagetrucktool.garbagetrucklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.domain.usecase.garbagetrucklist.GetGarbageTruckListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class GarbageTruckListViewModel @Inject constructor(
    private val getGarbageTruckListUseCase: Provider<GetGarbageTruckListUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<GarbageTruckViewState>(GarbageTruckViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private var garbageTruckList: List<GarbageTruck> = emptyList()
    private var filterString: String = ""

    init {
        getTrucks(null, true)
    }

    fun getTrucks(currentLatLng: LatLng?, forceUpdate: Boolean = false) {
        viewModelScope.launch {
            _viewState.value = GarbageTruckViewState.Loading

            getGarbageTruckListUseCase.get()
                .apply {
                    this.currentLatLng = currentLatLng
                    this.forceUpdate = forceUpdate
                }
                .invoke()
                .collect { result ->
                    when (result) {
                        is GetGarbageTruckListUseCase.Result.Success -> {
                            _viewState.emit(
                                GarbageTruckViewState.Success(result.list.toImmutableList())
                            )
                        }

                        is GetGarbageTruckListUseCase.Result.Empty -> {
                            _viewState.emit(GarbageTruckViewState.Empty)
                        }
                    }
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
            _viewState.emit(GarbageTruckViewState.Success(trucks.toImmutableList()))
        }
    }

    sealed class GarbageTruckViewState {
        object Loading : GarbageTruckViewState()
        object Empty : GarbageTruckViewState()
        data class Success(val garbageTrucks: ImmutableList<GarbageTruck>) : GarbageTruckViewState()
    }
}
