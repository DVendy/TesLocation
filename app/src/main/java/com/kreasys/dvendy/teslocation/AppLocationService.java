package com.kreasys.dvendy.teslocation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AppLocationService extends Service implements LocationListener {

    protected LocationManager locationManager;
    Location location;

    private static final long MIN_DISTANCE_FOR_UPDATE = 0;
    private static final long MIN_TIME_FOR_UPDATE = 0;

    public AppLocationService(Context context) {
        locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);
    }

    public Location getLocation(String provider) {
        if (locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider,
                    MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);
                return location;
            }
        }
        return null;
    }

    public String getCity(String provider, Context context) throws IOException {
        //System.out.println("==================================================================");
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        Location location = this.getLocation(provider);
        //System.out.println(location.getLatitude());
        List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        if (addresses.size() > 0){

            System.out.println(addresses.get(0).getLocality());
            return (addresses.get(0).getLocality());
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
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

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}