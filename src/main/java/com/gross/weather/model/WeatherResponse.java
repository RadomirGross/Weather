package com.gross.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Setter
@Getter
@ToString
public class WeatherResponse {
    private int locationIdFromDB;
    private String displayName;
    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;

    @Getter
    @Setter
    public static class Coord {
        private double lon;
        private double lat;
    }

    @Getter
    @Setter
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Getter
    @Setter
    public static class Main {
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;

        @JsonProperty("temp_min")
        private double tempMin;

        @JsonProperty("temp_max")
        private double tempMax;

        private int pressure;
        private int humidity;

        @JsonProperty("sea_level")
        private int seaLevel;

        @JsonProperty("grnd_level")
        private int grndLevel;


    }


@Getter
@Setter
public static class Wind {
    private double speed;
    private int deg;
}

@Getter
@Setter
public static class Clouds {
    private int all;
}

@Getter
@Setter
public static class Sys {
    private int type;
    private int id;
    private String country;
    private long sunrise;
    private long sunset;
}


}
