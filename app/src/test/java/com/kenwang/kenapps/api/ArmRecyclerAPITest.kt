package com.kenwang.kenapps.api

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Ignore("Ignore API test")
@OptIn(ExperimentalCoroutinesApi::class)
class ArmRecyclerAPITest {

    @Test
    fun testArmRecyclerAPI() = runTest {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://quality.data.gov.tw/")
            .build()
        val api = retrofit.create(ArmRecyclerService::class.java)
        val response = api.getArmRecyclerList()
        Truth.assertThat(response.isSuccessful).isTrue()
    }
}
