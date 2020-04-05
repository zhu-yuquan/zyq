package com.zyq.frechwind.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()//选择那些路径和api会生成document
//                .apis(RequestHandlerSelectors.any())//对所有api进行监控
//                .apis(RequestHandlerSelectors.basePackage("com.zyq"))
                .apis(RequestHandlerSelectors.withClassAnnotation(SwaggerDoc.class))
                .paths(PathSelectors.any())//对所有路径进行监控
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("EC接口")
                .description("zyq软件出品")
                .termsOfServiceUrl("http://www.zyq.com/")
                .contact("zyq软件")
                .version("0.0.1")
                .build();
    }
}