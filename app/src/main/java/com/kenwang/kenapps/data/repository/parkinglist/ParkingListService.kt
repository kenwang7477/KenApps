package com.kenwang.kenapps.data.repository.parkinglist

import com.google.gson.JsonArray
import retrofit2.Response
import retrofit2.http.GET

interface ParkingListService {

    // https://data.gov.tw/dataset/46944
    @GET("dq_download_json.php?nid=46944&md5_url=b25bf7a2fc1f634231115a7195a2a3ed")
    suspend fun getParkingList(): Response<JsonArray>
}
