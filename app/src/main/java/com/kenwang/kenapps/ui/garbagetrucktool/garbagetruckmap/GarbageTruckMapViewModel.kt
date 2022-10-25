package com.kenwang.kenapps.ui.garbagetrucktool.garbagetruckmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@HiltViewModel
class GarbageTruckMapViewModel @Inject constructor(
    private val garbageTruckRepository: GarbageTruckRepository
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
            getTrucks()
        }
    }

    fun stopRefresh() {
        timer?.cancel()
        timer = null
    }

    private fun getTrucks() {
        viewModelScope.launch {
            val truckList = garbageTruckRepository.getTrucks()
            _viewState.value = ViewState.Success(truckList)
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Success(val garbageTrucks: List<GarbageTruck>) : ViewState()
    }
}
