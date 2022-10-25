package com.kenwang.kenapps.ui.tvprogramlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvprogramlist.repository.TvProgramRepository
import com.kenwang.kenapps.data.model.TvProgram
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvProgramListViewModel @Inject constructor(
    private val tvProgramRepository: TvProgramRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<TvProgramListViewState>(TvProgramListViewState.Empty)
    val viewState = _viewState.asStateFlow()

    init {
        getProgramList(TvProgramRepository.TvProgramEnum.TvProgramList1)
    }

    fun getProgramList(eProgram: TvProgramRepository.TvProgramEnum) {
        viewModelScope.launch {
            val programs = tvProgramRepository.getProgramList(eProgram)
            _viewState.emit(TvProgramListViewState.Success(programs))
        }
    }

    sealed class TvProgramListViewState {
        data class Success(var programs: List<TvProgram>) : TvProgramListViewState()
        object Empty : TvProgramListViewState()
    }
}
