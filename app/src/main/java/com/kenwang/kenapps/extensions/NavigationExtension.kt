package com.kenwang.kenapps.extensions

import androidx.navigation.NavController
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.ui.ArmRecyclerListRoute
import com.kenwang.kenapps.ui.GarbageTruckListRoute
import com.kenwang.kenapps.ui.GarbageTruckMapRoute
import com.kenwang.kenapps.ui.MapLocationListRoute
import com.kenwang.kenapps.ui.MapLocationMapRoute
import com.kenwang.kenapps.ui.ParkingListRoute
import com.kenwang.kenapps.ui.ParkingMapRoute
import com.kenwang.kenapps.ui.SettingRoute
import com.kenwang.kenapps.ui.TextToSpeechRoute
import com.kenwang.kenapps.ui.TvProgramListRoute

fun NavController.toParkingList() = navigate(ParkingListRoute)

fun NavController.toParkingMap(
    parkingSpace: ParkingSpace
) {
    navigate(ParkingMapRoute(argParkingSpace = parkingSpace))
}

fun NavController.toGarbageTruckList() = navigate(GarbageTruckListRoute)

fun NavController.toGarbageTruckMap(garbageTruck: GarbageTruck) {
    navigate(GarbageTruckMapRoute(argGarbageTruck = garbageTruck))
}

fun NavController.toTvProgramList() = navigate(TvProgramListRoute)

fun NavController.toArmRecyclerList() = navigate(ArmRecyclerListRoute)

fun NavController.toSetting() {
    navigate(SettingRoute)
}

fun NavController.toMapLocationList() = navigate(MapLocationListRoute)

fun NavController.toMapLocationMap(
    longitude: Double?,
    latitude: Double?
) {
    if (longitude == null || latitude == null) {
        navigate(MapLocationMapRoute())
    } else {
        navigate(
            MapLocationMapRoute(
                argLongitude = longitude.toString(),
                argLatitude = latitude.toString()
            )
        )
    }
}

fun NavController.toTextToSpeech() = navigate(TextToSpeechRoute)
