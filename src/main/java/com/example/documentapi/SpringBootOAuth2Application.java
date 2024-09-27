package com.example.documentapi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.example.documentapi")
@ComponentScan(basePackages = {
        "com.example.documentapi",   // Paquete principal y subpaquetes
})
public class SpringBootOAuth2Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .profiles("sso")
                .sources(SpringBootOAuth2Application.class)
                .build()
                .run(args);
    }
}
