package com.kenwang.kenapps.ui

sealed class Screens(val route: String) {
    object Main : Screens("Main")
    object ParkingList : Screens("ParkingList")
    object ParkingMap : Screens("ParkingMap") {
        const val argParkingSpace = "argParkingSpace"
    }
    object GarbageTruckList : Screens("GarbageTruckList")
    object GarbageTruckMap : Screens("GarbageTruckMap") {
        const val argGarbageTruck = "argGarbageTruck"
    }
    object TvProgramList : Screens("TvProgramList")
    object ArmRecyclerList : Screens("ArmRecyclerList")
    object CctvList : Screens("CctvList")
    object CctvMap : Screens("CctvMap") {
        const val argCctvMonitor = "argCctvMonitor"
    }
    object Setting : Screens("Setting")
    object MapLocationList : Screens("MapLocationList")
    object MapLocationMap : Screens("MapLocationMap") {
        const val argLongitude = "argLongitude"
        const val argLatitude = "argLatitude"
    }
    object ChatGPT : Screens("ChatGPT")
    object TextToSpeech : Screens("TextToSpeech")
}
