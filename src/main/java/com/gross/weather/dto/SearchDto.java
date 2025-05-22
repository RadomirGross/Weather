package com.gross.weather.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {

    @NotBlank(message = "запрос не должен быть пустым")
    @Size(min = 1, max = 50,message = "Длинна должна быть от 1 до 50 символов")
    @Pattern(regexp = "^[A-Za-zА-Яа-яёЁ\\s\\-']+$",
            message = "Поисковый запрос не должен содержать цифры и недопустимые символы")
    String search;


}
