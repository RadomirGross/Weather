package com.gross.weather.testcontainer;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerHolder {

    public static final PostgreSQLContainer<?>  POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:17")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        POSTGRES_CONTAINER.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return POSTGRES_CONTAINER;
    }

    private PostgresContainerHolder() {}
}
