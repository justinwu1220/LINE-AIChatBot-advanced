package com.justinwu.lineaichatbot.model.cwa;


public class WeatherElement {
    private String dataTime;
    private ElementValue Wx;
    private ElementValue T;

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public ElementValue getWx() {
        return Wx;
    }

    public void setWx(ElementValue wx) {
        Wx = wx;
    }

    public ElementValue getT() {
        return T;
    }

    public void setT(ElementValue t) {
        T = t;
    }
}

