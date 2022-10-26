package com.kenwang.kenapps.data.repository.garbagetruck

import com.kenwang.kenapps.data.model.GarbageTruck

class GarbageTruckRepository(
    private val garbageTruckServerDataSource: GarbageTruckServerDataSource,
    private val garbageTruckLocalDataSource: GarbageTruckLocalDataSource
) {

    suspend fun getTrucks(): List<GarbageTruck> {
        return garbageTruckLocalDataSource.trucks.ifEmpty {
            garbageTruckLocalDataSource.trucks = garbageTruckServerDataSource.getTrucks()
            garbageTruckLocalDataSource.trucks
        }
    }
}
