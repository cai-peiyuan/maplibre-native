package com.mapabc.android.testapp.annotations;

import android.graphics.Color;

import org.maplibre.android.annotations.Polyline;
import org.maplibre.android.annotations.PolylineOptions;
import org.maplibre.android.geometry.LatLng;
import com.mapabc.android.testapp.activity.EspressoTest;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.mapabc.android.testapp.action.MapLibreMapAction;
import com.mapabc.android.testapp.activity.EspressoTest;

public class PolylineTest extends EspressoTest {

  @Test
  @Ignore
  public void addPolylineTest() {
    validateTestSetup();
    MapLibreMapAction.invoke(maplibreMap, (uiController, mapboxMap) -> {
      LatLng latLngOne = new LatLng();
      LatLng latLngTwo = new LatLng(1, 0);

      assertEquals("Polygons should be empty", 0, mapboxMap.getPolygons().size());

      final PolylineOptions options = new PolylineOptions();
      options.color(Color.BLUE);
      options.add(latLngOne);
      options.add(latLngTwo);
      Polyline polyline = mapboxMap.addPolyline(options);

      assertEquals("Polylines should be 1", 1, mapboxMap.getPolylines().size());
      assertEquals("Polyline id should be 0", 0, polyline.getId());
      assertEquals("Polyline points size should match", 2, polyline.getPoints().size());
      assertEquals("Polyline stroke color should match", Color.BLUE, polyline.getColor());
      mapboxMap.clear();
      assertEquals("Polyline should be empty", 0, mapboxMap.getPolylines().size());
    });
  }
}
