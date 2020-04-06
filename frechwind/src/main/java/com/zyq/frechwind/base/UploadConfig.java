package com.zyq.frechwind.base;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

@Configuration
@EnableAutoConfiguration
public class UploadConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File classRoot = new File(this.getClass().getClassLoader().getResource("").getPath());
        while (classRoot.getParentFile() != null) {
            classRoot = classRoot.getParentFile();
        }
        String root = classRoot.getAbsolutePath();
        if(root.endsWith("/") || root.endsWith("\\")){
            root = root + "upload";
        }else{
            root = root + "/upload";
        }

        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + root + "/");
        super.addResourceHandlers(registry);
    }
}