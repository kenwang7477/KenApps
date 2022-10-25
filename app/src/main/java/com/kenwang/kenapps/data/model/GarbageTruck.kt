package com.kenwang.kenapps.data.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class GarbageTruck(
    val linId: String = "",
    val car: String = "",
    val time: String = "",
    val location: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
): Parcelable {

    object NavigationType : NavType<GarbageTruck>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): GarbageTruck? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): GarbageTruck {
            return Gson().fromJson(value, GarbageTruck::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: GarbageTruck) {
            bundle.putParcelable(key, value)
        }
    }
}
