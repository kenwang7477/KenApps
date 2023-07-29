package com.kenwang.kenapps.data.repository.parkinglist

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonArray

class ParkingListService(private val httpClient: HttpClient) {

    // https://data.gov.tw/dataset/46944
    suspend fun getParkingList(): Result<JsonArray> {
        return runCatching {
            httpClient
                .get("dq_download_json.php?nid=46944&md5_url=25c99af1dc44fe2c6be61b3c79a006bd")
                .body()
        }
    }
}
