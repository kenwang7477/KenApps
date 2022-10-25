package com.kenwang.kenapps.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.kenwang.kenapps.R

object ChromeTabUtil {

    // on below line we are creating a function to open custom chrome tabs.
    fun openTab(context: Context, url: String) {
        // on below line we are creating a variable for
        // package name and specifying package name as
        // package of chrome application.
        val packageName = "com.android.chrome"

        // on below line we are creating a variable
        // for the activity and initializing it.
        val activity = (context as? Activity)

        // on below line we are creating a variable for
        // our builder and initializing it with
        // custom tabs intent
        val builder = CustomTabsIntent.Builder()

        // on below line we are setting show title
        // to true to display the title for
        // our chrome tabs.
        builder.setShowTitle(true)

        // on below line we are enabling instant
        // app to open if it is available.
        builder.setInstantAppsEnabled(true)

        // on below line we are setting tool bar color for our custom chrome tabs.
        builder.setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder().setToolbarColor(
                ContextCompat.getColor(context, R.color.purple_200)
            ).build()
        )

        // on below line we are creating a
        // variable to build our builder.
        val customBuilder = builder.build()

        // on below line we are checking if the package name is null or not.
        if (packageName != null) {
            // on below line if package name is not null
            // we are setting package name for our intent.
            customBuilder.intent.setPackage(packageName)

            // on below line we are calling launch url method
            // and passing url to it on below line.
            customBuilder.launchUrl(context, Uri.parse(url))
        } else {
            // this method will be called if the
            // chrome is not present in user device.
            // in this case we are simply passing URL
            // within intent to open it.
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            // on below line we are calling start
            // activity to start the activity.
            activity?.startActivity(i)
        }
    }
}
