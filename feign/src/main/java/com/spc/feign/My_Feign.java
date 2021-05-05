package com.spc.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class My_Feign {
    public static void main(String[] args) {
        new SpringApplicationBuilder(My_Feign.class)
                .properties("spring.config.location=classpath:/application.yml")
                .run(args);
        //SpringApplication.run(My_Feign.class, args);
    }
}
