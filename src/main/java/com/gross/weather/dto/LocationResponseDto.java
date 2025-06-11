package com.gross.weather.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



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
