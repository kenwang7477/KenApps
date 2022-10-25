package com.kenwang.kenapps.data.repository.garbagetruck

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET

interface GarbageTruckService {

    // https://api.kcg.gov.tw/ServiceList/Detail/aaf4ce4b-4ca8-43de-bfaf-6dc97e89cac0#collapseFive
    @GET("api/service/Get/aaf4ce4b-4ca8-43de-bfaf-6dc97e89cac0")
    suspend fun getTrucks(): Response<JsonObject>
}
