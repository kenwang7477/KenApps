package com.kenwang.kenapps.api

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.repository.cctvlist.CctvListService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Ignore("Ignore API test")
@OptIn(ExperimentalCoroutinesApi::class)
class CctvListAPITest {

    @Test
    fun testCctvListAPI() = runTest {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://quality.data.gov.tw/")
            .build()
        val api = retrofit.create(CctvListService::class.java)
        val response = api.getCctvList()
        Truth.assertThat(response.isSuccessful).isTrue()
    }
}
