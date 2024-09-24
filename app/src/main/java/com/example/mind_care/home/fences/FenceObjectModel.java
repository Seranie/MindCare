package com.example.mind_care.home.fences;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class FenceObjectModel {
    private GeoPoint location;
    private double radius;

    public FenceObjectModel(LatLng location, double radius) {
        this.location = new GeoPoint(location.latitude, location.longitude);
        this.radius = radius;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

}
