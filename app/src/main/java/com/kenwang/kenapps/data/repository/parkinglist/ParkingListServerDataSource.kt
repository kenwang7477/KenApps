package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpace

class ParkingListServerDataSource(
    private val parkingListClient: ParkingListClient
) {

    suspend fun getParkingList(): List<ParkingSpace> = parkingListClient.getParkingList()
}
