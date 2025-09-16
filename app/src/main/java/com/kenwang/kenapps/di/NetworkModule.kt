package com.kenwang.kenapps.di

import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckClient
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckMapper
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckService
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListClient
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListService
import com.kenwang.kenapps.data.repository.tdx.TdxClient
import com.kenwang.kenapps.data.repository.tdx.TdxService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json {
        isLenient = true
        explicitNulls = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideParkingListService(): ParkingListService {
        return ParkingListService(httpClient = getTdxHttpClient())
    }

    @Provides
    @Singleton
    fun provideParkingListClient(
        parkingListService: ParkingListService
    ) = ParkingListClient(
        parkingListService = parkingListService
    )

    @Provides
    @Singleton
    fun provideGarbageTruckService(): GarbageTruckService {
        return GarbageTruckService(httpClient = getKcgHttpClient())
    }

    @Provides
    @Singleton
    fun provideGarbageTruckClient(
        clientService: GarbageTruckService
    ) = GarbageTruckClient(
        garbageTruckService = clientService,
        garbageTruckMapper = GarbageTruckMapper()
    )

    @Provides
    @Singleton
    fun provideTdxClient(tdxService: TdxService) = TdxClient(tdxService = tdxService)

    @Provides
    @Singleton
    fun provideTdxService(): TdxService {
        return TdxService(httpClient = getTdxHttpClient())
    }

    private fun getOpenDataHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
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
    }

    private fun getKcgHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url("https://api.kcg.gov.tw/")
            }
            Logging {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
            install(ContentNegotiation) {
                json(json = json)
            }
        }
    }

    private fun getTdxHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url("https://tdx.transportdata.tw/")
            }
            Logging {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(json = json)
            }
        }
    }
}
