package com.gross.weather.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;




public class UserDto implements Serializable {

    public UserDto(String login, String password, String repeatPassword) {
        this.login = login;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public UserDto() {
        System.out.println("UserDto simple constructor");
    }

    @NotBlank(message = "Логин  не может быть пустым")
    @Size(min = 3, max = 30, message = "Логин должен быть от 3 до 30 символов")
    private String login;

    @NotBlank(message = "Пароль  не может быть пустым")
    @Size(min = 5, max = 60, message = "Пароль должен быть не менее 5 символов")
    private String password;

    @NotBlank(message = "Повторите пароль")
    @Size(min = 5, max = 60, message = "Пароль должен быть не менее 5 символов")
    private String repeatPassword;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
