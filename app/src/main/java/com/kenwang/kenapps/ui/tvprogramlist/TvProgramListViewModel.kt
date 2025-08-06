package com.kenwang.kenapps.ui.tvprogramlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvprogramlist.repository.TvProgramRepository
import com.kenwang.kenapps.data.model.TvProgram
import com.kenwang.kenapps.domain.usecase.tvprogramlist.GetTvProgramListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class TvProgramListViewModel @Inject constructor(
    private val getTvProgramListUseCase: Provider<GetTvProgramListUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<TvProgramListViewState>(TvProgramListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        getProgramList(TvProgramRepository.TvProgramEnum.TvProgramList1)
    }

    fun getProgramList(eProgram: TvProgramRepository.TvProgramEnum) {
        viewModelScope.launch {
            _viewState.emit(TvProgramListViewState.Loading)

            getTvProgramListUseCase.get().apply {
                this.eProgram = eProgram
            }
                .invoke()
                .collect { result ->
                    when (result) {
                        is GetTvProgramListUseCase.Result.Success -> {
                            _viewState.emit(
                                TvProgramListViewState.Success(result.list.toList())
                            )
                        }

                        is GetTvProgramListUseCase.Result.Empty -> {
                            _viewState.emit(TvProgramListViewState.Empty)
                        }
                    }
                }
        }
    }

    sealed class TvProgramListViewState {
        data class Success(var programs: List<TvProgram>) : TvProgramListViewState()
        object Loading : TvProgramListViewState()
        object Empty : TvProgramListViewState()
    }
}
