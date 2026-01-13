package com.dulara.figure_controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FigureControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FigureControllerApplication.class, args);
	}

}
