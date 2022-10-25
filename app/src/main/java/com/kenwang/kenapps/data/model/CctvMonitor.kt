package com.kenwang.kenapps.data.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class CctvMonitor(
    val cctvId: String = "",
    val roadsection: String = "",
    val locationpath: String = "",
    val startlocationpoint: String = "",
    val endlocationpoint: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
) : Parcelable {

    object NavigationType : NavType<CctvMonitor>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): CctvMonitor? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): CctvMonitor {
            return Gson().fromJson(value, CctvMonitor::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: CctvMonitor) {
            bundle.putParcelable(key, value)
        }
    }
}
