package com.mapabc.android.testapp.activity

import androidx.annotation.UiThread
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import com.mapabc.android.testapp.activity.espresso.EspressoTestActivity

/**
 * Base class for all tests using EspressoTestActivity as wrapper.
 *
 *
 * Loads "assets/streets.json" as style.
 *
 */
open class EspressoTest : _root_ide_package_.com.mapabc.android.testapp.activity.BaseTest() {
    override fun getActivityClass(): Class<*> {
        return EspressoTestActivity::class.java
    }

    @UiThread
    override fun initMap(maplibreMap: MapLibreMap) {
        maplibreMap.setStyle(Style.Builder().fromUri("asset://streets.json"))
        super.initMap(maplibreMap)
    }
}
