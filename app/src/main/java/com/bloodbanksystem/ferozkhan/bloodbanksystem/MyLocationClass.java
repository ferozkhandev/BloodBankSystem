package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationClass implements LocationListener {
    public Location location;
    MyLocationClass()
    {
        location.setLatitude(0.0);
        location.setLatitude(0.0);
    }
    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
