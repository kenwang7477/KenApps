package com.kenwang.kenapps.data.database.maplocation

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MapLocationDao {

    @Query("select * from $TABLE_NAME_MAP_LOCATION")
    fun getMapLocationList(): Flow<List<MapLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mapLocationEntity: MapLocationEntity)

    @Delete
    suspend fun delete(mapLocationEntity: MapLocationEntity)
}

const val TABLE_NAME_MAP_LOCATION = "map_location"
@Entity(tableName = TABLE_NAME_MAP_LOCATION)
data class MapLocationEntity(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val latitude: Double
)
