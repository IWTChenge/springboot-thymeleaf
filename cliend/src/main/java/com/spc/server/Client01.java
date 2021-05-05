package com.spc.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication()
@MapperScan("com.spc.server.dao")
@EnableEurekaClient//表明自己是一个eureka client.
public class Client01 {

	public static void main(String[] args) {
		//SpringApplication.run(FundApplication.class, args);
		new SpringApplicationBuilder(Client01.class)
				.properties("spring.config.location=classpath:/application.yml")
				.run(args);
	}

}
