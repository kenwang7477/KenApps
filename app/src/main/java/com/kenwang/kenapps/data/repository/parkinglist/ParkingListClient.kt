package com.kenwang.kenapps.data.repository.parkinglist

import com.google.gson.Gson
import com.kenwang.kenapps.data.model.ParkingSpace

class ParkingListClient(
    private val parkingListService: ParkingListService,
    private val gson: Gson
) {

    suspend fun getParkingList(): List<ParkingSpace> {
        return parkingListService.getParkingList().body()?.mapNotNull {
            try {
                gson.fromJson(it, ParkingSpace::class.java)
            } catch(e: Exception) {
                null
            }
        } ?: emptyList()
    }
}
