package com.mapabc.android.maps

import androidx.test.espresso.UiController
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import com.mapabc.android.testapp.action.MapLibreMapAction.invoke
import com.mapabc.android.testapp.activity.EspressoTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TransformTest : EspressoTest() {

    companion object {
        val initialCameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(12.0, 12.0), 12.0)!!
    }

    @Test
    fun mapboxMapScrollByWithPadding() {
        validateTestSetup()
        invoke(maplibreMap) { _: UiController, maplibreMap: MapLibreMap ->
            maplibreMap.moveCamera(initialCameraUpdate)
            maplibreMap.scrollBy(400.0f, 0.0f)
            val expectedCameraPosition = maplibreMap.cameraPosition

            maplibreMap.moveCamera(initialCameraUpdate)
            maplibreMap.setPadding(250, 250, 0, 0)
            maplibreMap.scrollBy(400.0f, 0.0f)
            val actualCameraPosition = maplibreMap.cameraPosition

            assertEquals("Camera position should match", expectedCameraPosition, actualCameraPosition)
        }
    }
}
