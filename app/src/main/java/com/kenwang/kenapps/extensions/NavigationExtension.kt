package com.kenwang.kenapps.extensions

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.data.model.ParkingSpaceCity
import com.kenwang.kenapps.ui.Screens

fun NavBackStack<NavKey>.addParkingList() = add(Screens.ParkingListRoute)

fun NavBackStack<NavKey>.addParkingMap(
    parkingSpace: ParkingSpace,
    city: ParkingSpaceCity
) {
    add(Screens.ParkingMapRoute(argParkingSpace = parkingSpace, argCity = city))
}

fun NavBackStack<NavKey>.addGarbageTruckList() = add(Screens.GarbageTruckListRoute)

fun NavBackStack<NavKey>.addGarbageTruckMap(garbageTruck: GarbageTruck) {
    add(Screens.GarbageTruckMapRoute(argGarbageTruck = garbageTruck))
}

fun NavBackStack<NavKey>.addTvProgramList() = add(Screens.TvProgramListRoute)

fun NavBackStack<NavKey>.addSetting() = add(Screens.SettingRoute)

fun NavBackStack<NavKey>.addMapLocationList() = add(Screens.MapLocationListRoute)

fun NavBackStack<NavKey>.addMapLocationMap(
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

fun NavBackStack<NavKey>.addTextToSpeech() = add(Screens.TextToSpeechRoute)
