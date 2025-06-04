package com.gross.weather.mapper;

import com.gross.weather.dto.LocationResponseDto;
import com.gross.weather.model.LocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationResponseMapper {

    LocationResponseMapper INSTANCE = Mappers.getMapper(LocationResponseMapper.class);

    LocationResponseDto toLocationResponseDto(LocationResponse locationResponse);

    List<LocationResponseDto> toLocationResponseDtoList(List<LocationResponse> locationResponseList);

}
