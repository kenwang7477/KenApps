package com.kenwang.kenapps.data.repository.garbagetruck

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class GarbageTruckService(private val httpClient: HttpClient) {

    // https://api.kcg.gov.tw/ServiceList/Detail/aaf4ce4b-4ca8-43de-bfaf-6dc97e89cac0#collapseFive
    suspend fun getTrucks(): Result<GarbageTruckResponse> {
        return runCatching {
            httpClient
                .get("api/service/Get/aaf4ce4b-4ca8-43de-bfaf-6dc97e89cac0")
                .body()
        }
    }
}
