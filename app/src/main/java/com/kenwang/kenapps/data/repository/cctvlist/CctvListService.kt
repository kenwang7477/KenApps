package com.kenwang.kenapps.data.repository.cctvlist

import com.google.gson.JsonArray
import retrofit2.Response
import retrofit2.http.GET

interface CctvListService {

    // https://data.gov.tw/dataset/43789
    @GET("dq_download_json.php?nid=43789&md5_url=38d0ac51ce4201bf8902aec858937548")
    suspend fun getCctvList(): Response<JsonArray>
}
