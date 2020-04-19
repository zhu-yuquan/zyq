package com.zyq.frechwind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.zyq")
@ServletComponentScan
@EnableScheduling
public class FrechwindApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FrechwindApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        SpringApplicationBuilder sab = builder.sources(FrechwindApplication.class);
        return sab;
    }
}
