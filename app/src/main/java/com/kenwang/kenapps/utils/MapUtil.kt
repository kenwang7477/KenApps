package com.kenwang.kenapps.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.kenwang.kenapps.R

object MapUtil {

    fun openGoogleMapByAddress(context: Context, address: String) {
        try {
            val mapUri: Uri =
                Uri.parse("geo:0,0?q=" + Uri.encode(address))
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(context.packageManager)?.let {
                context.startActivity(mapIntent)
            }
        } catch (e: Exception) {
            Toast
                .makeText(context, R.string.unknown_error, Toast.LENGTH_SHORT)
                .show()
        }
    }
}
