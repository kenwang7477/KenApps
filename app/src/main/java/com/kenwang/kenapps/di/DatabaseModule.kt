package com.kenwang.kenapps.di

import android.content.Context
import com.kenwang.kenapps.data.database.KenAppsDataBase
import com.kenwang.kenapps.data.database.maplocation.MapLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMapLocationDao(@ApplicationContext context: Context): MapLocationDao {
        return KenAppsDataBase.getInstance(context).mapLocationDao()
    }
}
