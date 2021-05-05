package com.spc.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer//表示是个注册中心
@SpringBootApplication
public class Server_ek {

	public static void main(String[] args) {
		//SpringApplication.run(FundApplication.class, args);
		new SpringApplicationBuilder(Server_ek.class)
				.properties("spring.config.location=classpath:/application.yml")
				.run(args);
	}

}
