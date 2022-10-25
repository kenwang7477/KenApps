package com.kenwang.kenapps.data.repository.cctvlist

import com.google.gson.Gson
import com.kenwang.kenapps.data.model.CctvMonitor

class CctvListClient(
    private val cctvListService: CctvListService,
    private val gson: Gson
) {

    suspend fun getCctvList(): List<CctvMonitor> {
        return cctvListService.getCctvList().body()?.mapNotNull {
            try {
                val response = gson.fromJson(it, CctvListResponse::class.java)
                CctvMonitorMapper.toCctvMonitor(response)
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
    }
}

object CctvMonitorMapper {

    fun toCctvMonitor(response: CctvListResponse): CctvMonitor {
        return CctvMonitor(
            cctvId = response.cctvId,
            roadsection = response.roadsection,
            locationpath = response.locationpath,
            startlocationpoint = response.startlocationpoint,
            endlocationpoint = response.endlocationpoint,
            longitude = response.longitude,
            latitude = response.latitude
        )
    }
}
