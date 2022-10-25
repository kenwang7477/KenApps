package com.kenwang.kenapps.data.repository.garbagetruck

import com.kenwang.kenapps.data.model.GarbageTruck

class GarbageTruckRepository(private val garbageTruckClient: GarbageTruckClient) {

    private var trucks = emptyList<GarbageTruck>()

    suspend fun getTrucks(): List<GarbageTruck> {
        return trucks.ifEmpty {
            trucks = garbageTruckClient.getTrucks()
            trucks
        }
    }
}
