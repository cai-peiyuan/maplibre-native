package com.mapabc.android.testapp.utils

import android.app.Activity
import android.content.Intent

object NavUtils {
    fun navigateHome(context: Activity) {
        context.startActivity(Intent(context, com.mapabc.android.testapp.activity.FeatureOverviewActivity::class.java))
        context.finish()
    }
}
