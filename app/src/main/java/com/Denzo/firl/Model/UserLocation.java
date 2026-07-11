package com.Denzo.firl.Model;

public class UserLocation {
    private double latitude;
    private double longitude;
    private String city;

    public UserLocation() {}

    public UserLocation(double latitude, double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
