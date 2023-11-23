package dev.sassine.simpleopenia.model;

public class WeatherResponse {
    public String location;
    public WeatherUnit unit;
    public int temperature;
    public String description;

    public WeatherResponse(String location, WeatherUnit unit, int temperature, String description) {
        this.location = location;
        this.unit = unit;
        this.temperature = temperature;
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WeatherUnit getUnit() {
        return unit;
    }

    public void setUnit(WeatherUnit unit) {
        this.unit = unit;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
