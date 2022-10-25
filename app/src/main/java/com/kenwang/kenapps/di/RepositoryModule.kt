package com.kenwang.kenapps.di

import com.example.tvprogramlist.repository.TvProgramRepository
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerClient
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerRepository
import com.kenwang.kenapps.data.repository.cctvlist.CctvListClient
import com.kenwang.kenapps.data.repository.cctvlist.CctvListRepository
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckClient
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckRepository
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListClient
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListRepository
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
    ) = ParkingListRepository(parkingListClient)

    @Singleton
    @Provides
    fun provideGarbageTruckRepository(
        garbageTruckClient: GarbageTruckClient
    ) = GarbageTruckRepository(garbageTruckClient)

    @Provides
    @Singleton
    fun provideTvProgramRepository(
        programDataSource: TvProgramDataSource
    ) = TvProgramRepository(programDataSource)

    @Provides
    @Singleton
    fun provideTvProgramDataSource() = TvProgramDataSource()

    @Provides
    @Singleton
    fun provideArmRecyclerRepository(
        armRecyclerClient: ArmRecyclerClient
    ) = ArmRecyclerRepository(armRecyclerClient)

    @Provides
    @Singleton
    fun provideCctvListRepository(
        cctvListClient: CctvListClient
    ) = CctvListRepository(cctvListClient)
}
