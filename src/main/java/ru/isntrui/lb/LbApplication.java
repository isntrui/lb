package ru.isntrui.lb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication(scanBasePackages = "ru.isntrui.lb")
@EnableJpaRepositories(basePackages = "ru.isntrui.lb.repositories")
public class LbApplication {

    public static void main(String[] args) {
        for (Map.Entry<?, ?> entry : System.getenv().entrySet()) {
            Logger.getLogger("app").log(Level.INFO, String.format("%-15s : %s%n", entry.getKey(), entry.getValue()));
        }
        SpringApplication.run(LbApplication.class, args);

    }

}
