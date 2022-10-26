package com.kenwang.kenapps.di

import com.example.tvprogramlist.repository.TvProgramRepository
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerClient
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerLocalDataSource
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerRepository
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerServerDataSource
import com.kenwang.kenapps.data.repository.cctvlist.CctvListClient
import com.kenwang.kenapps.data.repository.cctvlist.CctvListLocalDataSource
import com.kenwang.kenapps.data.repository.cctvlist.CctvListRepository
import com.kenwang.kenapps.data.repository.cctvlist.CctvListServerDataSource
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckClient
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckLocalDataSource
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckRepository
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckServerDataSource
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListClient
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListLocalDataSource
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListRepository
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListServerDataSource
import com.kenwang.kenapps.data.repository.tvprogramlist.TvProgramDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideParkingListRepository(
        parkingListClient: ParkingListClient
    ) = ParkingListRepository(
        ParkingListServerDataSource(parkingListClient),
        ParkingListLocalDataSource()
    )

    @Singleton
    @Provides
    fun provideGarbageTruckRepository(
        garbageTruckClient: GarbageTruckClient
    ) = GarbageTruckRepository(
        GarbageTruckServerDataSource(garbageTruckClient),
        GarbageTruckLocalDataSource()
    )

    @Provides
    @Singleton
    fun provideTvProgramRepository() = TvProgramRepository(TvProgramDataSource())

    @Provides
    @Singleton
    fun provideArmRecyclerRepository(
        armRecyclerClient: ArmRecyclerClient
    ) = ArmRecyclerRepository(
        ArmRecyclerServerDataSource(armRecyclerClient),
        ArmRecyclerLocalDataSource()
    )

    @Provides
    @Singleton
    fun provideCctvListRepository(
        cctvListClient: CctvListClient
    ) = CctvListRepository(
        CctvListServerDataSource(cctvListClient),
        CctvListLocalDataSource()
    )
}
