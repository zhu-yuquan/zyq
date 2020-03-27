package com.zyq.frechwind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoConfigurationPackage
public class FrechwindApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrechwindApplication.class, args);
    }

}
