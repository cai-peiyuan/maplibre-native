package com.mapabc.android.testapp.geometry;

import static org.junit.Assert.assertEquals;

import com.mapabc.android.testapp.action.MapLibreMapAction;
import com.mapabc.android.testapp.activity.BaseTest;
import com.mapabc.android.testapp.utils.TestConstants;

import org.junit.Assert;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.geometry.LatLngBounds;
import com.mapabc.android.testapp.activity.feature.QueryRenderedFeaturesBoxHighlightActivity;

import org.junit.Test;

/**
 * Instrumentation test to validate integration of LatLngBounds
 */
public class LatLngBoundsTest extends BaseTest {

  private static final double MAP_BEARING = 50;

  @Override
  protected Class getActivityClass() {
    return QueryRenderedFeaturesBoxHighlightActivity.class;
  }

  @Test
  public void testLatLngBounds() {
    // regression test for #9322
    validateTestSetup();
    MapLibreMapAction.invoke(maplibreMap, (uiController, mapboxMap) -> {
      LatLngBounds bounds = new LatLngBounds.Builder()
        .include(new LatLng(48.8589506, 2.2773457))
        .include(new LatLng(47.2383171, -1.6309316))
        .build();
      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
    });
  }

  @Test
  public void testLatLngBoundsBearing() {
    // regression test for #12549
    validateTestSetup();
    MapLibreMapAction.invoke(maplibreMap, (uiController, mapboxMap) -> {
      LatLngBounds bounds = new LatLngBounds.Builder()
        .include(new LatLng(48.8589506, 2.2773457))
        .include(new LatLng(47.2383171, -1.6309316))
        .build();
      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
      mapboxMap.moveCamera(CameraUpdateFactory.bearingTo(MAP_BEARING));
      Assert.assertEquals(
        "Initial bearing should match for latlngbounds",
        mapboxMap.getCameraPosition().bearing,
        MAP_BEARING,
        TestConstants.BEARING_DELTA
      );

      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
      assertEquals("Bearing should match after resetting latlngbounds",
        mapboxMap.getCameraPosition().bearing,
        MAP_BEARING,
        TestConstants.BEARING_DELTA);
    });
  }

}