package ru.isntrui.lb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;

@SpringBootApplication(scanBasePackages = "ru.isntrui.lb")
@EnableJpaRepositories(basePackages = "ru.isntrui.lb.repositories")
public class LbApplication {

    public static void main(String[] args) {
        for (Map.Entry<?, ?> entry : System.getenv().entrySet()) {
            System.out.printf("%-15s : %s%n", entry.getKey(), entry.getValue());
        }
        SpringApplication.run(LbApplication.class, args);

    }

}
