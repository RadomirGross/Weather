package com.gross.weather.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString

public class LocationSearchResult {
    List<LocationResponseDto> locations;
    String errorMessage;
}
