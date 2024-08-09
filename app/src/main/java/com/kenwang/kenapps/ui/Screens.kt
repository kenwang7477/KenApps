package com.kenwang.kenapps.ui

sealed class Screens(val route: String) {
    data object Main : Screens("Main")
    data object ParkingList : Screens("ParkingList")
    data object ParkingMap : Screens("ParkingMap") {
        const val argParkingSpace = "argParkingSpace"
    }
    data object GarbageTruckList : Screens("GarbageTruckList")
    data object GarbageTruckMap : Screens("GarbageTruckMap") {
        const val argGarbageTruck = "argGarbageTruck"
    }
    data object TvProgramList : Screens("TvProgramList")
    data object ArmRecyclerList : Screens("ArmRecyclerList")
    data object Setting : Screens("Setting")
    data object MapLocationList : Screens("MapLocationList")
    data object MapLocationMap : Screens("MapLocationMap") {
        const val argLongitude = "argLongitude"
        const val argLatitude = "argLatitude"
    }
    data object TextToSpeech : Screens("TextToSpeech")
}
