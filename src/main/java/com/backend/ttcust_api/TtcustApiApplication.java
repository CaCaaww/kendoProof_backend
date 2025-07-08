package com.backend.ttcust_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.backend.ttcust_api.controller.ttcustController;

@SpringBootApplication(scanBasePackageClasses = ttcustController.class)
public class TtcustApiApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TtcustApiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
		return builder.sources(TtcustApiApplication.class);
	}


}
