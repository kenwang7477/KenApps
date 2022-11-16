package com.kenwang.kenapps.domain.usecase.maplocation

import com.kenwang.kenapps.data.model.MapLocation
import com.kenwang.kenapps.data.repository.maplocation.MapLocationRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMapLocationListUseCase @Inject constructor(
    private val mapLocationRepository: MapLocationRepository
) {

    operator fun invoke() = flow {
        mapLocationRepository.getMapLocationList().collect { list ->
            if (list.isEmpty()) {
                emit(Result.Empty)
            } else {
                emit(Result.Success(list))
            }
        }
    }

    sealed class Result {
        data class Success(val list: List<MapLocation>) : Result()
        object Empty : Result()
    }
}
