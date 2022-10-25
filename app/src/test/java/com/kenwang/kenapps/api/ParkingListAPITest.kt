package com.kenwang.kenapps.api

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.repository.cctvlist.CctvListService
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class ParkingListAPITest {

    @Test
    fun testParkingListAPI() = runTest {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://quality.data.gov.tw/")
            .build()
        val api = retrofit.create(ParkingListService::class.java)
        val response = api.getParkingList()
        Truth.assertThat(response.isSuccessful).isTrue()
    }
}
