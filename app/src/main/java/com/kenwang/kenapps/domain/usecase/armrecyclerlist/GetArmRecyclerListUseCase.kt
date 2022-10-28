package com.kenwang.kenapps.domain.usecase.armrecyclerlist

import com.kenwang.kenapps.data.model.ArmRecycler
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArmRecyclerListUseCase @Inject constructor(
    private val armRecyclerRepository: ArmRecyclerRepository
) {

    operator fun invoke() = flow {
        val armRecyclerList = armRecyclerRepository.getArmRecyclerList()
        if (armRecyclerList.isEmpty()) {
            emit(Result.Empty)
        } else {
            emit(Result.Success(armRecyclerList))
        }
    }

    sealed class Result {
        object Empty : Result()
        data class Success(val list: List<ArmRecycler>) : Result()
    }
}
