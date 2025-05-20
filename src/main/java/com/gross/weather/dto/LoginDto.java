package com.gross.weather.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "Username не может быть пустым")
    private String login;

    @NotBlank(message = "Password не может быть пустым")
    private String password;

}
