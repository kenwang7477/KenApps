package com.kenwang.kenapps.api

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerService
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class GarbageTruckAPITest {

    @Test
    fun testGarbageTruckAPI() = runTest {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.kcg.gov.tw/")
            .build()
        val api = retrofit.create(GarbageTruckService::class.java)
        val response = api.getTrucks()
        Truth.assertThat(response.isSuccessful).isTrue()
    }
}
