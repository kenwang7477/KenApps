package com.kenwang.kenapps.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerClient
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerMapper
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerService
import com.kenwang.kenapps.data.repository.cctvlist.CctvListClient
import com.kenwang.kenapps.data.repository.cctvlist.CctvListService
import com.kenwang.kenapps.data.repository.chatgpt.ChatGPTClient
import com.kenwang.kenapps.data.repository.chatgpt.ChatGPTService
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckClient
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckMapper
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckService
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListClient
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListService
import com.kenwang.kenapps.data.repository.parkinglist.ParkingSpaceMapper
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        isLenient = true
        explicitNulls = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideParkingListService(): ParkingListService {
        return ParkingListService(httpClient = getOpenDataHttpClient())
    }

    @Provides
    @Singleton
    fun provideParkingListClient(
        parkingListService: ParkingListService
    ) = ParkingListClient(
        parkingListService = parkingListService,
        parkingSpaceMapper = ParkingSpaceMapper(),
        json = json
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
    fun provideArmRecyclerService(): ArmRecyclerService {
        return ArmRecyclerService(httpClient = getOpenDataHttpClient())
    }

    @Provides
    @Singleton
    fun provideArmRecyclerClient(
        armRecyclerService: ArmRecyclerService,
    ) = ArmRecyclerClient(
        armRecyclerService = armRecyclerService,
        armRecyclerMapper = ArmRecyclerMapper()
    )

    @Provides
    @Singleton
    fun provideCctvListService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ) = govOpenDataRetrofit<CctvListService>(okHttpClient = okHttpClient, gson = gson)

    @Provides
    @Singleton
    fun provideCctvListClient(
        cctvListService: CctvListService,
        gson: Gson
    ) = CctvListClient(cctvListService = cctvListService, gson = gson)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
//        val interceptor = HttpLoggingInterceptor().apply {
//            level = if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor.Level.BODY
//            } else {
//                HttpLoggingInterceptor.Level.NONE
//            }
//        }
//        return OkHttpClient.Builder().addInterceptor(interceptor).build()
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideChatGPTService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ) = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.openai.com/")
            .client(okHttpClient)
            .build()
            .create(ChatGPTService::class.java)

    @Provides
    @Singleton
    fun provideChatGPTClient(
        chatGPTService: ChatGPTService
    ) = ChatGPTClient(chatGPTService = chatGPTService)

    private inline fun <reified T: Any> govOpenDataRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("https://quality.data.gov.tw/")
        .client(okHttpClient)
        .build()
        .create(T::class.java)

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
}
