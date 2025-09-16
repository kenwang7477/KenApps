package com.kenwang.kenapps.ui

import androidx.navigation3.runtime.NavKey
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceCity
import kotlinx.serialization.Serializable

@Serializable
sealed class Screens : NavKey {
    @Serializable
    data object MainRoute : Screens()

    @Serializable
    data object ParkingListRoute : Screens()

    @Serializable
    data class ParkingMapRoute(
        val argParkingSpace: ParkingSpace,
        val argCity: ParkingSpaceCity
    ) : Screens()

    @Serializable
    data object GarbageTruckListRoute : Screens()

    @Serializable
    data class GarbageTruckMapRoute(val argGarbageTruck: GarbageTruck) : Screens()

    @Serializable
    data object TvProgramListRoute : Screens()

    @Serializable
    data object SettingRoute : Screens()

    @Serializable
    data object MapLocationListRoute : Screens()

    @Serializable
    data class MapLocationMapRoute(
        val argLongitude: String? = null,
        val argLatitude: String? = null
    ) : Screens()

    @Serializable
    data object TextToSpeechRoute : Screens()
}
