package com.kenwang.kenapps.extensions

import androidx.navigation3.runtime.NavBackStack
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.ui.Screens

fun NavBackStack.addParkingList() = add(Screens.ParkingListRoute)

fun NavBackStack.addParkingMap(
    parkingSpace: ParkingSpace
) {
    add(Screens.ParkingMapRoute(argParkingSpace = parkingSpace))
}

fun NavBackStack.addGarbageTruckList() = add(Screens.GarbageTruckListRoute)

fun NavBackStack.addGarbageTruckMap(garbageTruck: GarbageTruck) {
    add(Screens.GarbageTruckMapRoute(argGarbageTruck = garbageTruck))
}

fun NavBackStack.addTvProgramList() = add(Screens.TvProgramListRoute)

fun NavBackStack.addArmRecyclerList() = add(Screens.ArmRecyclerListRoute)

fun NavBackStack.addSetting() = add(Screens.SettingRoute)

fun NavBackStack.addMapLocationList() = add(Screens.MapLocationListRoute)

fun NavBackStack.addMapLocationMap(
    longitude: Double?,
    latitude: Double?
) {
    if (longitude == null || latitude == null) {
        add(Screens.MapLocationMapRoute())
    } else {
        add(
            Screens.MapLocationMapRoute(
                argLongitude = longitude.toString(),
                argLatitude = latitude.toString()
            )
        )
    }
}

fun NavBackStack.addTextToSpeech() = add(Screens.TextToSpeechRoute)
