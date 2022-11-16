package com.kenwang.kenapps.ui.maplocation.maplocationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.MapLocation
import com.kenwang.kenapps.domain.usecase.maplocation.DeleteMapLocationUseCase
import com.kenwang.kenapps.domain.usecase.maplocation.GetMapLocationListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class MapLocationListViewModel @Inject constructor(
    private val getMapLocationListUseCase: Provider<GetMapLocationListUseCase>,
    private val deleteMapLocationUseCase: Provider<DeleteMapLocationUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<MapLocationListViewState>(MapLocationListViewState.Empty)
    val viewState = _viewState.asStateFlow()

    init {
        getMapLocationList()
    }

    fun getMapLocationList() {
        viewModelScope.launch {
            _viewState.emit(MapLocationListViewState.Loading)

            getMapLocationListUseCase.get().invoke().collect { result ->
                when (result) {
                    is GetMapLocationListUseCase.Result.Success -> {
                        _viewState.emit(MapLocationListViewState.Success(result.list.toImmutableList()))
                    }
                    is GetMapLocationListUseCase.Result.Empty -> {
                        _viewState.emit(MapLocationListViewState.Empty)
                    }
                }
            }
        }
    }

    fun deleteMapLocation(mapLocation: MapLocation) {
        viewModelScope.launch {
            deleteMapLocationUseCase.get().invoke(mapLocation)
        }
    }

    sealed class MapLocationListViewState {
        data class Success(val list: ImmutableList<MapLocation>) : MapLocationListViewState()
        object Empty : MapLocationListViewState()
        object Loading : MapLocationListViewState()
    }
}
