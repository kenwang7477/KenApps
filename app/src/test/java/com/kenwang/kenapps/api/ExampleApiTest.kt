package com.kenwang.kenapps.api

import com.google.gson.JsonArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

@Ignore("Ignore API test")
@OptIn(ExperimentalCoroutinesApi::class)
class ApiTest {

    @Test
    fun testApi() = runTest {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://quality.data.gov.tw/")
            .build()

        val api = retrofit.create(TestAPI::class.java)
        val response = api.getCctvList()
        print("response = ${response.body()}")
    }
}

interface TestAPI {

    @GET("dq_download_json.php?nid=43789&md5_url=38d0ac51ce4201bf8902aec858937548")
    suspend fun getCctvList(): Response<JsonArray>
}
