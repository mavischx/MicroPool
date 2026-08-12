package com.micropool.league_service;

import com.micropool.league_service.model.MatchResult;
import com.micropool.league_service.repository.MatchResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Testcontainers
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class LeagueServiceApplicationTests {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

	@Autowired
	private MatchResultRepository repository;

	@BeforeEach
	void setUp() {
		repository.deleteAll();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry)
	{
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@Test
	void shouldSaveMatchResultAndRetrieve()
	{
		MatchResult matchResult = new MatchResult();
		matchResult.setId(UUID.randomUUID());
		matchResult.setWinner("John");
		matchResult.setLoser("Jane");
		matchResult.setTimestamp(Instant.now());

		repository.save(matchResult);

        List<MatchResult> list = repository.findAll();
		assertThat(list).hasSize(1);
		assertThat(list.get(0).getWinner()).isEqualTo("John");
		assertThat(list.get(0).getMatchId()).isEqualTo(matchResult.getMatchId());
		assertThat(list.get(0).getLoser()).isEqualTo("Jane");
		assertThat(list.get(0).getTimestamp()).isNotNull();
	}
	@Test
	void shouldCountWinsPerPlayer() {
		MatchResult result1 = new MatchResult();
		result1.setId(UUID.randomUUID());
		result1.setWinner("Alice");
		result1.setLoser("Bob");
		result1.setTimestamp(Instant.now());

		MatchResult result2 = new MatchResult();
		result2.setId(UUID.randomUUID());
		result2.setWinner("Alice");
		result2.setLoser("Charlie");
		result2.setTimestamp(Instant.now());

		MatchResult result3 = new MatchResult();
		result3.setId(UUID.randomUUID());
		result3.setWinner("Bob");
		result3.setLoser("Alice");
		result3.setTimestamp(Instant.now());

		repository.save(result1);
		repository.save(result2);
		repository.save(result3);

		assertThat(repository.countByWinner("Alice")).isEqualTo(2);
		assertThat(repository.countByWinner("Bob")).isEqualTo(1);
		assertThat(repository.countByWinner("Charlie")).isEqualTo(0);
	}

	@Test
	void shouldCountLossesPerPlayer() {
		MatchResult result1 = new MatchResult();
		result1.setId(UUID.randomUUID());
		result1.setWinner("Alice");
		result1.setLoser("Bob");
		result1.setTimestamp(Instant.now());

		MatchResult result2 = new MatchResult();
		result2.setId(UUID.randomUUID());
		result2.setWinner("Charlie");
		result2.setLoser("Bob");
		result2.setTimestamp(Instant.now());

		MatchResult result3 = new MatchResult();
		result3.setId(UUID.randomUUID());
		result3.setWinner("Bob");
		result3.setLoser("Alice");
		result3.setTimestamp(Instant.now());

		repository.save(result1);
		repository.save(result2);
		repository.save(result3);

		assertThat(repository.countByLoser("Bob")).isEqualTo(2);
		assertThat(repository.countByLoser("Alice")).isEqualTo(1);
		assertThat(repository.countByLoser("Charlie")).isEqualTo(0);
	}






}
