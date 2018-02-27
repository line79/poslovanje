package com.sargije.web.poslovanje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication
public class Poslovanje_Application extends SpringBootServletInitializer  {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	    return application.sources(Poslovanje_Application.class);
	}
	 
	public static void main(String[] args) {
		SpringApplication.run(Poslovanje_Application.class, args);
	}
	
}