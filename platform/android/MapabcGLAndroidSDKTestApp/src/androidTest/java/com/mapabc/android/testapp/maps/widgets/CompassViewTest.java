package com.mapabc.android.testapp.maps.widgets;

import org.junit.Assert;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import com.mapabc.android.testapp.action.WaitAction;
import com.mapabc.android.testapp.activity.EspressoTest;
import com.mapabc.android.testapp.utils.TestConstants;

import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import com.mapabc.android.testapp.action.MapLibreMapAction;
import com.mapabc.android.testapp.activity.EspressoTest;

public class CompassViewTest extends EspressoTest {

  @Test
  public void testDefault() {
    validateTestSetup();
    onView(withTagValue(is("compassView"))).check(matches(not(isDisplayed())));
  }

  @Test
  @Ignore("No explanation given")
  public void testVisible() {
    validateTestSetup();
    MapLibreMapAction.invoke(maplibreMap, (uiController, mapboxMap) -> {
      mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder()
          .bearing(45)
          .zoom(1)
          .target(new LatLng())
            .build()
      ));
      uiController.loopMainThreadForAtLeast(500);
    });
    onView(withTagValue(is("compassView"))).check(matches(isDisplayed()));
  }

  @Test
  @Ignore("No explanation given")
  public void testClick() {
    validateTestSetup();
    MapLibreMapAction.invoke(maplibreMap, (uiController, mapboxMap) -> mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(
      new CameraPosition.Builder()
        .bearing(45)
        .zoom(1)
        .target(new LatLng())
        .build()
    )));
    onView(withTagValue(is("compassView"))).perform(click());
    WaitAction.invoke(500);
    onView(withTagValue(is("compassView"))).check(matches(not(isDisplayed())));
    MapLibreMapAction.invoke(maplibreMap, (uiController, mapboxMap) -> {
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      Assert.assertEquals("Camera bearing should face north, ", 0, cameraPosition.bearing, TestConstants.BEARING_DELTA);
    });
  }
}

