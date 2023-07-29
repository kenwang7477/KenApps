package com.kenwang.kenapps.data.repository.garbagetruck

import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.data.repository.base.APIClientBase
import com.kenwang.kenapps.data.repository.base.APIClientBaseImpl

class GarbageTruckClient(
    private val garbageTruckService: GarbageTruckService,
    private val garbageTruckMapper: GarbageTruckMapper
) : APIClientBase by APIClientBaseImpl() {

    suspend fun getTrucks(): List<GarbageTruck> {
        val result = garbageTruckService.getTrucks()
        return result.getOrNull()
            ?.trucks
            ?.map { garbageTruckMapper.toGarbageTruck(it) }
            ?: emptyList()
    }
}

class GarbageTruckMapper {

    fun toGarbageTruck(bean: GarbageTruckBean): GarbageTruck {
        return GarbageTruck(
            linId = bean.linId ?: "",
            car = bean.car ?: "",
            time = bean.time ?: "",
            location = bean.location ?: "",
            longitude = bean.longitude ?: 0.0,
            latitude = bean.latitude ?: 0.0
        )
    }
}
