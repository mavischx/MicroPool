package com.micropool.league_service;

import org.springframework.boot.SpringApplication;

public class TestLeagueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(LeagueServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
