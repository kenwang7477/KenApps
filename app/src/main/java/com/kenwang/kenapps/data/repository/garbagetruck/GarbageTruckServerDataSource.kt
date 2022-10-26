package com.kenwang.kenapps.data.repository.garbagetruck

import com.kenwang.kenapps.data.model.GarbageTruck

class GarbageTruckServerDataSource(
    private val garbageTruckClient: GarbageTruckClient
) {

    suspend fun getTrucks(): List<GarbageTruck> = garbageTruckClient.getTrucks()
}
