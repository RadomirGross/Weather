package com.gross.weather.testcontainer;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerHolder {

    public static final PostgreSQLContainer<?>  POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        POSTGRES_CONTAINER.start();
    }

    private PostgresContainerHolder() {}
}
