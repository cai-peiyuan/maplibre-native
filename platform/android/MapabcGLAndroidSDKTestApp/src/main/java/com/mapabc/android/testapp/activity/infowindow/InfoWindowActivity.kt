package com.mapabc.android.testapp.activity.infowindow

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.maplibre.android.annotations.Marker
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapLibreMap.OnInfoWindowClickListener
import org.maplibre.android.maps.MapLibreMap.OnInfoWindowCloseListener
import org.maplibre.android.maps.MapLibreMap.OnInfoWindowLongClickListener
import org.maplibre.android.maps.MapLibreMap.OnMapLongClickListener
import org.maplibre.android.maps.OnMapReadyCallback
import org.maplibre.android.maps.Style
import com.mapabc.android.testapp.R
import java.text.DecimalFormat

/**
 * Test activity showcasing using the InfoWindow API above Washington D.C.
 *
 *
 * Allows to test mulitple concurrently open InfoWindows.
 *
 */
class InfoWindowActivity :
    AppCompatActivity(),
    OnMapReadyCallback,
    OnInfoWindowCloseListener,
    OnInfoWindowClickListener,
    OnInfoWindowLongClickListener {
    private lateinit var mapabcMap: MapLibreMap
    private lateinit var mapView: MapView
    private var customMarker: Marker? = null

    /**
     * 地图长按事件
     */
    private val mapLongClickListener = OnMapLongClickListener { point ->
        if (customMarker != null) {
            // Remove previous added marker
            mapabcMap.removeAnnotation(customMarker!!)
            customMarker = null
        }

        // Add marker on long click location with default marker image
        customMarker = mapabcMap.addMarker(
            MarkerOptions()
                .title("Custom Marker")
                .snippet(
                    DecimalFormat("#.#####").format(point.latitude) + ", " +
                        DecimalFormat("#.#####").format(point.longitude)
                )
                .position(point)
        )
        true
    }

    /**
     * activity 创建事件
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infowindow)
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(maplibreMap: MapLibreMap) {
        this.mapabcMap = maplibreMap
        maplibreMap.setStyle(Style.getPredefinedStyle("Streets")) { style: Style? ->
            addMarkers()
            addInfoWindowListeners()
        }
    }

    private fun addMarkers() {
        mapabcMap.addMarker(
            MarkerOptions()
                .title("Intersection")
                .snippet("H St NW with 15th St NW")
                .position(LatLng(38.9002073, -77.03364419))
        )
        mapabcMap.addMarker(
            MarkerOptions().title("Intersection")
                .snippet("E St NW with 17th St NW")
                .position(LatLng(38.8954236, -77.0394623))
        )
        mapabcMap.addMarker(
            MarkerOptions().title("The Ellipse").position(LatLng(38.89393, -77.03654))
        )
        mapabcMap.addMarker(MarkerOptions().position(LatLng(38.89596, -77.03434)))
        mapabcMap.addMarker(
            MarkerOptions().snippet("Lafayette Square").position(LatLng(38.89949, -77.03656))
        )
        val marker = mapabcMap.addMarker(
            MarkerOptions()
                .title("White House")
                .snippet(
                    "The official residence and principal workplace of the President of the United States, " +
                        "located at 1600 Pennsylvania Avenue NW in Washington, D.C. It has been the residence of every" +
                        "U.S. president since John Adams in 1800."
                )
                .position(LatLng(38.897705003219784, -77.03655168667463))
        )

        // open InfoWindow at startup
        mapabcMap.selectMarker(marker)
    }

    private fun addInfoWindowListeners() {
        mapabcMap.onInfoWindowCloseListener = this
        mapabcMap.addOnMapLongClickListener(mapLongClickListener)
        mapabcMap.onInfoWindowClickListener = this
        mapabcMap.onInfoWindowLongClickListener = this
    }

    private fun toggleConcurrentInfoWindow(allowConcurrentInfoWindow: Boolean) {
        mapabcMap.deselectMarkers()
        mapabcMap.isAllowConcurrentMultipleOpenInfoWindows = allowConcurrentInfoWindow
    }

    private fun toggleDeselectMarkersOnTap(deselectMarkersOnTap: Boolean) {
        mapabcMap.uiSettings.isDeselectMarkersOnTap = deselectMarkersOnTap
    }

    override fun onInfoWindowClick(marker: Marker): Boolean {
        Toast.makeText(applicationContext, "OnClick: " + marker.title, Toast.LENGTH_LONG).show()
        // returning true will leave the info window open
        return false
    }

    override fun onInfoWindowClose(marker: Marker) {
        Toast.makeText(applicationContext, "OnClose: " + marker.title, Toast.LENGTH_LONG).show()
    }

    override fun onInfoWindowLongClick(marker: Marker) {
        Toast.makeText(applicationContext, "OnLongClick: " + marker.title, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapabcMap != null) {
            mapabcMap.removeOnMapLongClickListener(mapLongClickListener)
        }
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_infowindow, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_concurrent_infowindow -> {
                toggleConcurrentInfoWindow(!item.isChecked)
                item.isChecked = !item.isChecked
                true
            }
            R.id.action_toggle_deselect_markers_on_tap -> {
                toggleDeselectMarkersOnTap(!item.isChecked)
                item.isChecked = !item.isChecked
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
