package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceCity

class ParkingListServerDataSource(
    private val parkingListClient: ParkingListClient
) {

    suspend fun getParkingList(
        authorization: String,
        parkingSpaceCity: ParkingSpaceCity
    ): List<ParkingSpace> {
        return parkingListClient.getParkingList(
            authorization = authorization,
            parkingSpaceCity = parkingSpaceCity
        )
    }
}
