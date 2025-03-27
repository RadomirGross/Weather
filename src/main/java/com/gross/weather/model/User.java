package com.gross.weather.model;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@Entity

@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "login", nullable = false, unique = true, length = 30)
    private String login;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "login = " + login + ", " +
                "password = " + password + ")";
    }
}