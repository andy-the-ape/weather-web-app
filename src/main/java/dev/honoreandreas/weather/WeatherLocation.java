package dev.honoreandreas.weather;

public enum WeatherLocation {
    COPENHAGEN(55.67594, 12.56553),
    ODENSE(55.39594, 10.38831),
    NEW_YORK(40.7128, -74.0060),
    LONDON(51.5074, -0.1278),
    PARIS(48.8566, 2.3522),
    TOKYO(35.6895, 139.6917);

    private final double latitude;
    private final double longitude;

    WeatherLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
