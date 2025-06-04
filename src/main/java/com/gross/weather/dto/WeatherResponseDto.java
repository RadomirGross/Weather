package com.gross.weather.dto;


import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class WeatherResponseDto {
    private int locationIdFromDB;
    private String displayName;
    private String description;
    private String icon;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private String error;

}