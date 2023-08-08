package com.mapabc.android.testapp.style

import android.view.View
import androidx.test.espresso.UiController
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import com.mapabc.android.testapp.R
import com.mapabc.android.testapp.activity.EspressoTest
import com.mapabc.android.testapp.utils.ResourceUtils.readRawResource
import org.junit.Assert
import org.junit.Test
import java.io.IOException

/**
 * Tests around style loading
 */
class StyleLoaderTest : EspressoTest() {
    @Test
    fun testSetGetStyleJsonString() {
        validateTestSetup()
        _root_ide_package_.com.mapabc.android.testapp.action.MapLibreMapAction.invoke(
            maplibreMap
        ) { uiController: UiController?, maplibreMap: MapLibreMap ->
            try {
                val expected =
                    readRawResource(
                        rule.activity,
                        R.raw.local_style
                    )
                maplibreMap.setStyle(Style.Builder().fromJson(expected))
                val actual = maplibreMap.style!!.json
                Assert.assertEquals("Style json should match", expected, actual)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
    }

    @Test
    fun testDefaultStyleLoadWithActivityLifecycleChange() {
        validateTestSetup()
        _root_ide_package_.com.mapabc.android.testapp.action.MapLibreMapAction.invoke(
            maplibreMap
        ) { uiController: UiController?, maplibreMap: MapLibreMap ->
            try {
                val expected =
                    readRawResource(
                        rule.activity,
                        R.raw.local_style
                    )
                maplibreMap.setStyle(Style.Builder().fromJson(expected))

                // fake activity stop/start
                val mapView =
                    rule.activity.findViewById<View>(R.id.mapView) as MapView
                mapView.onPause()
                mapView.onStop()
                mapView.onStart()
                mapView.onResume()
                val actual = maplibreMap.style!!.json
                Assert.assertEquals(
                    "Style URL should be empty",
                    "",
                    maplibreMap.style!!.uri
                )
                Assert.assertEquals("Style json should match", expected, actual)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
    }
}
