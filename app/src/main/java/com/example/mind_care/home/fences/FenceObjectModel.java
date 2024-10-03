package com.example.mind_care.home.fences;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class FenceObjectModel {
    private String fenceId;
    private GeoPoint location;
    private double radius;
    private String fenceName;

    public FenceObjectModel(LatLng location, double radius, String fenceName) {
        this.location = new GeoPoint(location.latitude, location.longitude);
        this.radius = radius;
        this.fenceName = fenceName;
    }

    public FenceObjectModel(){}

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


    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public String getFenceId() {
        return fenceId;
    }

    public void setFenceId(String fenceId) {
        this.fenceId = fenceId;
    }
}
