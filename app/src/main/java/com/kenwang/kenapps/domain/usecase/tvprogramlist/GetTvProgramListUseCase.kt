package com.kenwang.kenapps.domain.usecase.tvprogramlist

import com.example.tvprogramlist.repository.TvProgramRepository
import com.kenwang.kenapps.data.model.TvProgram
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTvProgramListUseCase @Inject constructor(
    private val tvProgramRepository: TvProgramRepository
) {

    var eProgram: TvProgramRepository.TvProgramEnum = TvProgramRepository.TvProgramEnum.TvProgramList1

    operator fun invoke() = flow {
        val programList = tvProgramRepository.getProgramList(eProgram)
        if (programList.isEmpty()) {
            emit(Result.Empty)
        } else {
            emit(Result.Success(programList))
        }
    }

    sealed class Result {
        object Empty : Result()
        data class Success(val list: List<TvProgram>) : Result()
    }
}
