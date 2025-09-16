package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpaceBean
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class ParkingListService(private val httpClient: HttpClient) {

    suspend fun getParkingList(authorization: String, city: String): Result<ParkingSpaceBean> {
        return runCatching {
            httpClient
                .get("api/basic/v1/Parking/OffStreet/CarPark/City/$city") {
                    header(HttpHeaders.Authorization, authorization)
                }
                .body()
        }
    }
}
