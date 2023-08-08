package com.mapabc.android.testapp.maps

import androidx.test.espresso.UiController
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import com.mapabc.android.testapp.activity.EspressoTest
import com.mapabc.android.testapp.utils.TestingAsyncUtils
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class StyleLoadTest : EspressoTest() {

    @Test
    fun updateSourceAfterStyleLoad() {
        validateTestSetup()
        _root_ide_package_.com.mapabc.android.testapp.action.MapLibreMapAction.invoke(maplibreMap) { uiController: UiController, maplibreMap: MapLibreMap ->
            val source = GeoJsonSource("id")
            val layer = SymbolLayer("id", "id")
            maplibreMap.setStyle(Style.Builder().withSource(source).withLayer(layer))
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            maplibreMap.setStyle(Style.Builder().fromUrl(Style.getPredefinedStyle("Streets")))
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            source.setGeoJson("{}")
        }
    }
}
