package com.kenwang.kenapps.api

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Ignore
import org.junit.Test

@Ignore("Ignore API test")
class ArmRecyclerAPITest {

    @Test
    fun testArmRecyclerAPI() = runTest {
        val json = Json {
            isLenient = true
            explicitNulls = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        val httpClient = HttpClient(OkHttp) {
            defaultRequest {
                url("https://quality.data.gov.tw/")
            }
            Logging {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
            install(ContentNegotiation) {
                json(json = json)
            }
        }
        val armRecyclerService = ArmRecyclerService(httpClient = httpClient)
        val result = armRecyclerService.getArmRecyclerList()
        Truth.assertThat(result.isSuccess).isTrue()
    }
}
