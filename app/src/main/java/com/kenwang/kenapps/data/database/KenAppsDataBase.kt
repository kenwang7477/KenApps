package com.kenwang.kenapps.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kenwang.kenapps.data.database.maplocation.MapLocationDao
import com.kenwang.kenapps.data.database.maplocation.MapLocationEntity

@Database(
    entities = [MapLocationEntity::class],
    version = 1
)
abstract class KenAppsDataBase : RoomDatabase() {

    abstract fun mapLocationDao(): MapLocationDao

    companion object {

        private const val DATABASE_NAME = "ken_apps.db"
        private lateinit var database: KenAppsDataBase

        fun getInstance(context: Context): KenAppsDataBase {
            if (!this::database.isInitialized) {
                synchronized(KenAppsDataBase::class) {
                    database = Room.databaseBuilder(
                        context,
                        KenAppsDataBase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return database
        }
    }
}
