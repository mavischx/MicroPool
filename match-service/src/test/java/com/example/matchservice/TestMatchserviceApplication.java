package com.example.matchservice;

import org.springframework.boot.SpringApplication;

public class TestMatchserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MatchserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
