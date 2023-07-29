package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceBean
import com.kenwang.kenapps.data.repository.base.APIClientBase
import com.kenwang.kenapps.data.repository.base.APIClientBaseImpl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ParkingListClient(
    private val parkingListService: ParkingListService,
    private val parkingSpaceMapper: ParkingSpaceMapper,
    private val json: Json
) : APIClientBase by APIClientBaseImpl() {

    suspend fun getParkingList(): List<ParkingSpace> {
        val result = parkingListService.getParkingList()
        val jsonArray = result.getOrNull() ?: return emptyList()
        return jsonArray.mapNotNull {
            try {
                val bean = json.decodeFromJsonElement<ParkingSpaceBean>(it)
                parkingSpaceMapper.toParkingSpace(bean)
            } catch (e: Exception) {
                null
            }
        }
    }
}

class ParkingSpaceMapper {

    fun toParkingSpace(parkingSpaceBean: ParkingSpaceBean): ParkingSpace {
        return ParkingSpace(
            area = parkingSpaceBean.area,
            type = parkingSpaceBean.type,
            name = parkingSpaceBean.name,
            address = parkingSpaceBean.address,
            largeCarCount = parkingSpaceBean.largeCarCount,
            normalCarCount = parkingSpaceBean.normalCarCount,
            bicycleCount = parkingSpaceBean.bicycleCount,
            charges = parkingSpaceBean.charges,
            information = parkingSpaceBean.information
        )
    }
}
