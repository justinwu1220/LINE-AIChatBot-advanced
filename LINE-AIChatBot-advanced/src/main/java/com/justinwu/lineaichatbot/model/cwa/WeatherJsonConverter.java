package com.justinwu.lineaichatbot.model.cwa;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherJsonConverter {
    public List<WeatherData> convert(String json) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode location = root.path("records").path("locations").get(0).path("location").get(0);

            // 處理 Wx 和 T 元素
            JsonNode wxElements = location.path("weatherElement").get(0).path("time");
            JsonNode tElements = location.path("weatherElement").get(1).path("time");

            for (int i = 0; i < wxElements.size(); i++) {
                WeatherData data = new WeatherData();
                data.setDataTime(wxElements.get(i).path("startTime").asText());

                ElementValue wx = new ElementValue();
                wx.setValue(wxElements.get(i).path("elementValue").get(0).path("value").asText());
                wx.setMeasures(wxElements.get(i).path("elementValue").get(0).path("measures").asText());
                data.setWx(wx);

                ElementValue t = new ElementValue();
                t.setValue(tElements.get(i).path("elementValue").get(0).path("value").asText());
                t.setMeasures(tElements.get(i).path("elementValue").get(0).path("measures").asText());
                data.setT(t);

                weatherDataList.add(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherDataList;
    }
}
