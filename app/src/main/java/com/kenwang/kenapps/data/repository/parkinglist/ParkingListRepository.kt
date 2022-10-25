package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpace

class ParkingListRepository(
    private val parkingListClient: ParkingListClient
) {
    private var parkingList: List<ParkingSpace> = emptyList()

    suspend fun getParkingList(): List<ParkingSpace> {
        return parkingList.ifEmpty {
            parkingList = parkingListClient.getParkingList()
            parkingList
        }
    }
}
