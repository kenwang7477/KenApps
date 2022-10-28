package com.kenwang.kenapps.di

import com.google.gson.Gson
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerClient
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerMapper
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerService
import com.kenwang.kenapps.data.repository.cctvlist.CctvListClient
import com.kenwang.kenapps.data.repository.cctvlist.CctvListService
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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideParkingListService(
        okHttpClient: OkHttpClient
    ) = govOpenDataRetrofit<ParkingListService>(okHttpClient)

    @Provides
    @Singleton
    fun provideParkingListClient(
        parkingListService: ParkingListService,
        gson: Gson
    ) = ParkingListClient(parkingListService, ParkingSpaceMapper(), gson)

    @Provides
    @Singleton
    fun provideGarbageTruckClient(
        clientService: GarbageTruckService,
        gson: Gson
    ) = GarbageTruckClient(clientService, GarbageTruckMapper(), gson)

    @Provides
    @Singleton
    fun provideGarbageTruckService(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.kcg.gov.tw/")
        .client(okHttpClient)
        .build()
        .create(GarbageTruckService::class.java)

    @Provides
    @Singleton
    fun provideArmRecyclerService(
        okHttpClient: OkHttpClient
    ) = govOpenDataRetrofit<ArmRecyclerService>(okHttpClient)

    @Provides
    @Singleton
    fun provideArmRecyclerClient(
        armRecyclerService: ArmRecyclerService,
        gson: Gson
    ) = ArmRecyclerClient(armRecyclerService, ArmRecyclerMapper(), gson)

    @Provides
    @Singleton
    fun provideCctvListService(
        okHttpClient: OkHttpClient
    ) = govOpenDataRetrofit<CctvListService>(okHttpClient)

    @Provides
    @Singleton
    fun provideCctvListClient(
        cctvListService: CctvListService,
        gson: Gson
    ) = CctvListClient(cctvListService, gson)

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

    private inline fun <reified T: Any> govOpenDataRetrofit(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://quality.data.gov.tw/")
        .client(okHttpClient)
        .build()
        .create(T::class.java)
}
