package com.kenwang.kenapps.ui.garbagetrucktool.garbagetruckmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.domain.usecase.garbagetrucklist.GetGarbageTruckListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import javax.inject.Provider
import kotlin.concurrent.fixedRateTimer

@HiltViewModel
class GarbageTruckMapViewModel @Inject constructor(
    private val getGarbageTruckListUseCase: Provider<GetGarbageTruckListUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private var timer: Timer? = null

    init {
        getTrucks()
    }

    override fun onCleared() {
        super.onCleared()
        stopRefresh()
    }

    fun startRefresh(period: Long) {
        timer = fixedRateTimer(name = "RefreshTimer", initialDelay = 0, period = period) {
            getTrucks(true)
        }
    }

    fun stopRefresh() {
        timer?.cancel()
        timer = null
    }

    private fun getTrucks(forceUpdate: Boolean = false) {
        viewModelScope.launch {
            getGarbageTruckListUseCase.get()
                .apply {
                    this.forceUpdate = forceUpdate
                }
                .invoke()
                .collect { result ->
                when (result) {
                    is GetGarbageTruckListUseCase.Result.Success -> {
                        _viewState.value = ViewState.Success(result.list)
                    }
                    is GetGarbageTruckListUseCase.Result.Error,
                    is GetGarbageTruckListUseCase.Result.Empty -> Unit
                }
            }
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Success(val garbageTrucks: List<GarbageTruck>) : ViewState()
    }
}
