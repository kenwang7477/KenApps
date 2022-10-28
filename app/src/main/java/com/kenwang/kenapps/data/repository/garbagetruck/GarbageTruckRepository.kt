package com.kenwang.kenapps.data.repository.garbagetruck

import com.kenwang.kenapps.data.model.GarbageTruck

class GarbageTruckRepository(
    private val garbageTruckServerDataSource: GarbageTruckServerDataSource,
    private val garbageTruckLocalDataSource: GarbageTruckLocalDataSource
) {

    suspend fun getTrucks(forceUpdate: Boolean = false): List<GarbageTruck> {
        if (forceUpdate) {
            garbageTruckLocalDataSource.trucks = emptyList()
        }
        return garbageTruckLocalDataSource.trucks.ifEmpty {
            garbageTruckLocalDataSource.trucks = garbageTruckServerDataSource.getTrucks()
            garbageTruckLocalDataSource.trucks
        }
    }
}
