package ru.isntrui.lb.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LB Tool API") // Set your custom title here
                        .version("0.0.1")
                        .description("""
                                API for LB Tool app.
                                
                                To test it choose some endpoint bellow and click "Try it on" or just use Postman.
                                
                                Made by Artem Akopian
                                """)
                        .contact(new Contact()
                                .name("Github")
                                .url("https://github.com/isntrui"))
                        .license(new License()
                                .name("WTFPL")
                                .url("http://www.wtfpl.net")));
    }
}