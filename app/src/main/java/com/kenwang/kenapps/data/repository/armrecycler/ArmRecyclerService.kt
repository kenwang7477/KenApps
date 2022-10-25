package com.kenwang.kenapps.data.repository.armrecycler

import com.google.gson.JsonArray
import retrofit2.Response
import retrofit2.http.GET

interface ArmRecyclerService {

    // https://data.gov.tw/dataset/86420
    @GET("dq_download_json.php?nid=86420&md5_url=7ddfe00e6779090ba10ec9e10b1d6aea")
    suspend fun getArmRecyclerList(): Response<JsonArray>
}
