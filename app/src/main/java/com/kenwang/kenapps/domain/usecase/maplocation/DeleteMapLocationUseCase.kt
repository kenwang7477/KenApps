package com.kenwang.kenapps.domain.usecase.maplocation

import com.kenwang.kenapps.data.model.MapLocation
import com.kenwang.kenapps.data.repository.maplocation.MapLocationRepository
import javax.inject.Inject

class DeleteMapLocationUseCase @Inject constructor(
    private val mapLocationRepository: MapLocationRepository
) {

    suspend operator fun invoke(mapLocation: MapLocation) {
        mapLocationRepository.deleteMapLocationEntity(mapLocation)
    }
}
