package com.justinwu.lineaichatbot.model.cwa;

import java.util.List;

public class WeatherData {
    private String city;
    private String district;
    List<WeatherElement> weatherElementList;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<WeatherElement> getWeatherElementList() {
        return weatherElementList;
    }

    public void setWeatherElementList(List<WeatherElement> weatherElementList) {
        this.weatherElementList = weatherElementList;
    }
}
