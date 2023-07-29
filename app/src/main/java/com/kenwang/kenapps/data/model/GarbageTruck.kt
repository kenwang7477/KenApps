package com.kenwang.kenapps.data.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

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

        @OptIn(ExperimentalSerializationApi::class)
        private val json = Json {
            isLenient = true
            explicitNulls = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        override fun get(bundle: Bundle, key: String): GarbageTruck? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): GarbageTruck {
            return json.decodeFromString(value)
        }

        override fun put(bundle: Bundle, key: String, value: GarbageTruck) {
            bundle.putParcelable(key, value)
        }
    }
}
