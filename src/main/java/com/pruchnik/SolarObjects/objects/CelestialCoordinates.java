package com.pruchnik.SolarObjects.objects;

import lombok.Getter;
import lombok.Setter;

//made in case for different APIs

@Getter
@Setter
public class CelestialCoordinates {
    private double longitude; // measured in degrees along the ecliptic
    private double latitude; // measured in degrees above or below the ecliptic plane
    private double distance; // measured in astronomical units (AU) from the Sun

    public CelestialCoordinates(double longitude, double latitude, double distance) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
    }
}