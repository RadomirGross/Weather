package com.gross.weather.mapper;


import com.gross.weather.dto.WeatherResponseDto;
import com.gross.weather.model.WeatherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface WeatherResponseMapper {
    WeatherResponseMapper INSTANCE = Mappers.getMapper(WeatherResponseMapper.class);

    @Mapping(source = "locationIdFromDB", target = "locationIdFromDB")
    @Mapping(source = "displayName", target = "displayName")
    @Mapping(target = "description", expression = "java(weatherResponse.getWeather() != null && !weatherResponse.getWeather().isEmpty() ? weatherResponse.getWeather().get(0).getDescription() : \"\")")
    @Mapping(target = "icon", expression = "java(weatherResponse.getWeather() != null && !weatherResponse.getWeather().isEmpty() ? weatherResponse.getWeather().get(0).getIcon() : \"\")")
    @Mapping(target = "temperature", expression = "java(Math.round(weatherResponse.getMain().getTemp()))")
    @Mapping(target = "feelsLike", expression = "java(Math.round(weatherResponse.getMain().getFeelsLike()))")
    @Mapping(target = "humidity", expression = "java(weatherResponse.getMain().getHumidity())")
    WeatherResponseDto toWeatherResponseDto(WeatherResponse weatherResponse);

}
