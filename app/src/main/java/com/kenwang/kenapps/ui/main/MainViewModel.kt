package com.kenwang.kenapps.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow<MainViewState>(MainViewState.Empty)
    val viewState = _viewState.asStateFlow()

    init {
        getMainList()
    }

    private fun getMainList() {
        val items = listOf(
            ListItem.ParkingMap,
            ListItem.GarbageTruckMap,
            ListItem.TvProgramList,
            ListItem.ArmRecyclerMap,
            ListItem.CctvList
        )
        viewModelScope.launch {
            _viewState.emit(MainViewState.Success(items))
        }
    }

    sealed class MainViewState {
        object Empty : MainViewState()
        data class Success(val items: List<ListItem>) : MainViewState()
    }

    enum class ListItem {
        ParkingMap,
        GarbageTruckMap,
        TvProgramList,
        ArmRecyclerMap,
        CctvList,
    }
}
