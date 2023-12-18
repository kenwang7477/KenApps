package com.kenwang.kenapps.domain.usecase.maplocation

import com.kenwang.kenapps.data.model.MapLocation
import com.kenwang.kenapps.data.repository.maplocation.MapLocationRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMapLocationListUseCase @Inject constructor(
    private val mapLocationRepository: MapLocationRepository
) {

    operator fun invoke() = mapLocationRepository.getMapLocationList().map { list ->
        if (list.isEmpty()) {
            Result.Empty
        } else {
            Result.Success(list)
        }
    }

    sealed class Result {
        data class Success(val list: List<MapLocation>) : Result()
        data object Empty : Result()
    }
}
