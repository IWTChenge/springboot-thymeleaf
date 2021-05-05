package com.spc.myfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MyfeignApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MyfeignApplication.class)
				.properties("spring.config.location=classpath:/application.yml")
				.run(args);
		//SpringApplication.run(MyfeignApplication.class, args);
	}

}
