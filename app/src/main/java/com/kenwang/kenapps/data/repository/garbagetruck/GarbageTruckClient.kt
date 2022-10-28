package com.kenwang.kenapps.data.repository.garbagetruck

import com.google.gson.Gson
import com.kenwang.kenapps.data.model.GarbageTruck

class GarbageTruckClient(
    private val garbageTruckService: GarbageTruckService,
    private val garbageTruckMapper: GarbageTruckMapper,
    private val gson: Gson
) {

    suspend fun getTrucks(): List<GarbageTruck> {
        return garbageTruckService.getTrucks().body()?.getAsJsonArray("data")?.mapNotNull {
            try {
                val bean = gson.fromJson(it, GarbageTruckBean::class.java)
                garbageTruckMapper.toGarbageTruck(bean)
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
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
