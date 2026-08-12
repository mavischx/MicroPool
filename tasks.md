# MicroPool - Task List

## Rack 1: Rack 'Em Up (Skeleton + Docker)

### Shot Scientist
- [x] Generate shot-service Spring Boot project (Web + Actuator)
- [x] Configure `application.properties` (port, actuator health)
- [x] Create Dockerfile
- [x] Add shot-service block to `docker-compose.yml`
- [x] Verify `mvn clean package` passes
- [x] Verify health endpoint responds in Docker

### Match Master
- [ ] Generate match-service Spring Boot project (Web + Actuator)
- [ ] Fix groupId to `com.micropool`
- [ ] Create a working Dockerfile (not placeholder)
- [ ] Add match-service block to `docker-compose.yml`
- [ ] Verify health endpoint responds in Docker

### Pool Hall Manager
- [x] Generate league-service Spring Boot project (Web + Actuator + JPA + PostgreSQL)
- [ ] Add league-service + postgres blocks to `docker-compose.yml`
- [ ] Add Docker volume for PostgreSQL data
- [ ] Verify all three services start with `docker compose up --build`
- [ ] Verify container-to-container networking (service DNS names work)

---

## Rack 2: Break Shot (Core Logic)

### Shot Scientist
- [ ] Write unit tests for `ShotCalculator` — all outcome buckets
- [ ] Implement `ShotCalculator` with deterministic formula
- [ ] Write unit tests for edge cases (boundary values, invalid inputs)
- [ ] Create `ShotRequest` / `ShotResponse` DTOs
- [ ] Create `ShotOutcome` enum
- [ ] Write MockMvc integration test for `POST /shots`
- [ ] Implement `ShotController` with validation
- [ ] Verify endpoint works end-to-end in Docker

### Match Master
- [ ] Implement match creation (`POST /matches`)
- [ ] Implement match state model (players, balls, turns)
- [ ] Implement take-shot endpoint (`POST /matches/{id}/shots`)
- [ ] Call Shot Service via RestClient/RestTemplate
- [ ] Apply shot outcome to match state (remove balls, switch turns)
- [ ] Implement foul handling (turn switches on FOUL)
- [ ] Implement win condition (8-ball rule)
- [ ] Implement `GET /matches/{id}`
- [ ] Write tests for turn logic, fouls, and win conditions

### Pool Hall Manager
- [ ] Configure PostgreSQL connection in league-service
- [ ] Create `match_results` entity/table
- [ ] Implement `POST /results` endpoint
- [ ] Implement `GET /leaderboard` endpoint
- [ ] Write basic persistence tests

---

## Rack 3: Don't Scratch (Failure Handling)

### Shot Scientist
- [ ] Ensure Shot Service can be stopped independently
- [ ] Help Match Master test timeout behaviour

### Match Master
- [ ] Define expected behaviour when Shot Service is down
- [ ] Add timeout to Shot Service HTTP call (e.g., 3 seconds)
- [ ] Return meaningful error to caller on failure
- [ ] Ensure match state is NEVER partially updated on failure
- [ ] Write test for unavailable-service scenario

---

## Rack 4: Test the Table (Testing + Testcontainers)

### Shot Scientist
- [ ] Ensure unit tests cover all `ShotCalculator` branches
- [ ] Write Spring integration test for full request/response cycle
- [ ] Verify determinism: same inputs always produce same output

### Match Master
- [ ] Unit tests for game rules (fouls, win conditions, turn switching)
- [ ] Integration test for a multi-shot match flow

### Pool Hall Manager
- [ ] Add Testcontainers PostgreSQL test for league-service
- [ ] Verify persist-and-read-back of match results
- [ ] Verify leaderboard query with multiple results

---

## Rack 5: Run the Pool Hall (End-to-End)

### All Team
- [ ] Match Service sends completed result to League Service
- [ ] Full flow works: create match → shots → winner → leaderboard updated
- [ ] `docker compose up --build` starts everything (including postgres)
- [ ] Docker volume persists leaderboard data across restarts
- [ ] Write README with startup, test, and play instructions
- [ ] Another team can clone and run without source changes

---

## Consistency Checklist

- [ ] All services use groupId `com.micropool`
- [ ] All folder names are hyphenated (`shot-service`, `match-service`, `league-service`)
- [ ] All services run internally on port 8080
- [ ] All Dockerfiles use `eclipse-temurin:17-jre` (runtime stage)
- [ ] `.gitattributes` set to `* text=auto eol=lf` in each service
- [ ] docker-compose.yml contains all three services + postgres
- [ ] Health endpoints exposed: `/actuator/health`
