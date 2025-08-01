package com.study.springStarter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.study.springStarter.mapper")
public class SpringStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringStarterApplication.class, args);
	}

}
