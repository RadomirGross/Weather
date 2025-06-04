package com.gross.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;


@Getter
@Setter
@ToString
public class LocationResponseDto {

    private String name;
    private String lat;
    private String lon;
    private String country;
    private String state;

}
