package com.kenwang.kenapps.di

import android.content.Context
import com.example.tvprogramlist.repository.TvProgramRepository
import com.kenwang.kenapps.data.database.maplocation.MapLocationDao
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerClient
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerLocalDataSource
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerRepository
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerServerDataSource
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckClient
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckLocalDataSource
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckRepository
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckServerDataSource
import com.kenwang.kenapps.data.repository.maplocation.MapLocationLocalDataSource
import com.kenwang.kenapps.data.repository.maplocation.MapLocationMapper
import com.kenwang.kenapps.data.repository.maplocation.MapLocationRepository
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListClient
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListLocalDataSource
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListRepository
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListServerDataSource
import com.kenwang.kenapps.data.repository.systempreference.SystemPreferenceLocalDataStore
import com.kenwang.kenapps.data.repository.systempreference.SystemPreferenceRepository
import com.kenwang.kenapps.data.repository.tvprogramlist.TvProgramDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        parkingListServerDataSource = ParkingListServerDataSource(parkingListClient = parkingListClient),
        parkingListLocalDataSource = ParkingListLocalDataSource()
    )

    @Singleton
    @Provides
    fun provideGarbageTruckRepository(
        garbageTruckClient: GarbageTruckClient
    ) = GarbageTruckRepository(
        garbageTruckServerDataSource = GarbageTruckServerDataSource(garbageTruckClient = garbageTruckClient),
        garbageTruckLocalDataSource = GarbageTruckLocalDataSource()
    )

    @Provides
    @Singleton
    fun provideTvProgramRepository() = TvProgramRepository(tvProgramDataSource = TvProgramDataSource())

    @Provides
    @Singleton
    fun provideArmRecyclerRepository(
        armRecyclerClient: ArmRecyclerClient
    ) = ArmRecyclerRepository(
        armRecyclerServerDataSource = ArmRecyclerServerDataSource(armRecyclerClient = armRecyclerClient),
        armRecyclerLocalDataSource = ArmRecyclerLocalDataSource()
    )

    @Provides
    @Singleton
    fun provideSystemPreferenceRepository(
        @ApplicationContext context: Context
    ): SystemPreferenceRepository = SystemPreferenceRepository(
        systemPreferenceLocalDataStore = SystemPreferenceLocalDataStore(context = context)
    )

    @Provides
    @Singleton
    fun provideMapLocationRepository(
        mapLocationDao: MapLocationDao
    ) = MapLocationRepository(
        mapLocationLocalDataSource = MapLocationLocalDataSource(
            mapLocationDao = mapLocationDao,
            mapLocationMapper = MapLocationMapper()
        )
    )
}
