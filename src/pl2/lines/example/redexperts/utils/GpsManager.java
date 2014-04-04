package pl2.lines.example.redexperts.utils;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GpsManager extends Observable implements LocationListener {

  private boolean         isGPSEnabled      = false;
  private Location        location;
  private double          latitude;
  private double          longitude;
  private LocationManager locationManager;
  private Observer        observer;
  private boolean         isGpsPaused;

  public GpsManager(Context context) {
    observer = (Observer) context;
    addObserver(observer);
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  public void setGpsPaused(){
    isGpsPaused = true;
  }
  
  public void setGpsResumed(){
    isGpsPaused = false;
  }
  public void getLocation() {
    try {
      if (isGPSEnabled && !isGpsPaused) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        if (locationManager != null) {
          location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
          if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public LocationManager getLocationManager() {
    return locationManager;
  }

  public void stopGPS() {
    if (locationManager != null) {
      locationManager.removeUpdates(this);
    }
  }
  
  public void removeObserver() {
    deleteObservers();
  }

  public double getLatitude() {
    if (location != null)
      latitude = location.getLatitude();

    return latitude;
  }

  public double getLongitude() {
    if (location != null) {
      longitude = location.getLongitude();
    }
    return longitude;
  }

  public boolean isGpsEnabled() {
    return isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

  }

  @Override
  public void onLocationChanged(Location location) {
    if (location != null) {
      latitude = location.getLatitude();
      longitude = location.getLongitude();
      this.location = location;
    }
    setChanged();
    notifyObservers();
  }

  public float getDistanceBetween(Double endLatitude, Double endLongitude) {
    Location destTmp = new Location("dest");
    destTmp.setLatitude(endLatitude);
    destTmp.setLongitude(endLongitude);
    if (location != null)
      return location.distanceTo(destTmp);
    else
      return 0;
  }

  @Override
  public void onProviderDisabled(String provider) {
  }

  @Override
  public void onProviderEnabled(String provider) {
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
  }
}
