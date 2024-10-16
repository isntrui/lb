package ru.isntrui.lb;

import org.springframework.boot.SpringApplication;

public class TestLbApplication {

    public static void main(String[] args) {
        SpringApplication.from(LbApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
