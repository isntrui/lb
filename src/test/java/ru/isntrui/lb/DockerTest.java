package ru.isntrui.lb;

import org.testcontainers.containers.PostgreSQLContainer;

public class DockerTest {
    public static void main(String[] args) {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
        postgres.start();
        System.out.println("PostgreSQL container started: " + postgres.getJdbcUrl());
    }
}
