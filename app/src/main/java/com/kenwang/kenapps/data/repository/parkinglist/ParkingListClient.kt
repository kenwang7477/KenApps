package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceBean
import com.kenwang.kenapps.data.model.ParkingSpaceCity
import com.kenwang.kenapps.data.repository.base.APIClientBase
import com.kenwang.kenapps.data.repository.base.APIClientBaseImpl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ParkingListClient(
    private val parkingListService: ParkingListService
) : APIClientBase by APIClientBaseImpl() {

    suspend fun getParkingList(
        authorization: String,
        parkingSpaceCity: ParkingSpaceCity
    ): List<ParkingSpace> {
        val result = parkingListService.getParkingList(
            authorization = authorization,
            city = parkingSpaceCity.cityName
        )
        return result.getOrNull()?.carParks?.map { it.toParkingSpace() } ?: emptyList()
    }
}

private fun ParkingSpaceBean.ParkingSpaceDetailBean.toParkingSpace(): ParkingSpace {
    return ParkingSpace(
        name = this.parkName.twName ?: "",
        description = this.description,
        fareDescription = this.fareDescription,
        address = this.address,
        latitude = this.position.latitude,
        longitude = this.position.longitude,
        isPublic = this.isPublic == 1
    )
}
