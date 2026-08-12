# MicroPool - Design Document

## Context

We are building a simplified 8-ball pool system as three Spring Boot microservices communicating over REST, packaged in Docker, orchestrated with Docker Compose. The goal is a working distributed system — not realistic physics.

**Team roles:**
- **Match Master** — Match Service, pool rules, game-state tests
- **Shot Scientist** — Shot Service, deterministic shot algorithm, edge cases
- **Pool Hall Manager** — League Service, persistence, Compose, Testcontainers

## Architecture

```
┌─────────────┐       POST /shots       ┌──────────────┐
│   Match     │ ───────────────────────► │    Shot      │
│   Service   │ ◄─────────────────────── │    Service   │
│  :8080      │       ShotResponse       │   :8080      │
└──────┬──────┘                          └──────────────┘
       │
       │ POST /results (on match complete)
       ▼
┌──────────────┐        ┌──────────────────┐
│   League     │◄───────│   PostgreSQL     │
│   Service    │        │   (volume)       │
│   :8080      │        └──────────────────┘
└──────────────┘
```

**Docker Compose networking:**
- Services communicate via Compose DNS names: `http://shot-service:8080`, `http://league-service:8080`
- External ports: match-service → 8080, shot-service → 8081, league-service → 8082
- Internal: all services run on port 8080 inside their containers

## REST API Contracts

### Shot Service — `POST /shots`

**Request:**
```json
{
  "angle": 37,
  "power": 72
}
```

**Validation:**
- `angle`: integer, 0–359 (inclusive)
- `power`: integer, 1–100 (inclusive)

**Response (200 OK):**
```json
{
  "outcome": "POT_TWO",
  "inputAngle": 37,
  "inputPower": 72
}
```

**Response (400 Bad Request):**
```json
{
  "error": "Invalid input",
  "message": "angle must be between 0 and 359"
}
```

**Outcome enum values:** `FOUL`, `MISS`, `POT_ONE`, `POT_TWO`

**Deterministic formula:**
```
resultCode = (angle + power) % 10
0       → FOUL    (cue ball potted)
1 to 4  → MISS
5 to 8  → POT_ONE (one ball potted)
9       → POT_TWO (two balls potted)
```

---

### Match Service — `POST /matches`

**Request:**
```json
{
  "player1": "Alice",
  "player2": "Bob"
}
```

**Response (201 Created):**
```json
{
  "matchId": "uuid",
  "player1": "Alice",
  "player2": "Bob",
  "currentTurn": "Alice",
  "status": "IN_PROGRESS",
  "solidsRemaining": 7,
  "stripesRemaining": 7,
  "eightBallOnTable": true
}
```

---

### Match Service — `POST /matches/{id}/shots`

**Request:**
```json
{
  "angle": 37,
  "power": 72
}
```

**Response (200 OK):**
```json
{
  "shotOutcome": "POT_TWO",
  "ballsPotted": [3, 5],
  "foul": false,
  "turnContinues": true,
  "currentTurn": "Alice",
  "matchStatus": "IN_PROGRESS",
  "solidsRemaining": 5,
  "stripesRemaining": 7,
  "eightBallOnTable": true
}
```

---

### Match Service — `GET /matches/{id}`

Returns full match state (same shape as creation response plus shot history if desired).

---

### League Service — `POST /results`

**Request:**
```json
{
  "matchId": "uuid",
  "winner": "Alice",
  "loser": "Bob",
  "completedAt": "2026-08-12T10:30:00Z"
}
```

**Response (201 Created):**
```json
{
  "resultId": "uuid",
  "matchId": "uuid",
  "winner": "Alice",
  "loser": "Bob"
}
```

---

### League Service — `GET /leaderboard`

**Response (200 OK):**
```json
[
  { "player": "Alice", "wins": 3, "losses": 1 },
  { "player": "Bob", "wins": 2, "losses": 2 }
]
```

## Data Models

### Shot Service
No persistence. Stateless — pure computation.

### Match Service
In-memory state (ConcurrentHashMap or similar). Each match holds:
```
Match {
  id: UUID
  player1: String
  player2: String
  currentTurn: String (player1 or player2)
  player1Balls: Set<Integer>  (assigned after first pot)
  player2Balls: Set<Integer>
  eightBallOnTable: boolean
  status: IN_PROGRESS | FINISHED
  winner: String (nullable)
}
```

### League Service
PostgreSQL table:
```sql
CREATE TABLE match_results (
  id UUID PRIMARY KEY,
  match_id UUID NOT NULL,
  winner VARCHAR(100) NOT NULL,
  loser VARCHAR(100) NOT NULL,
  completed_at TIMESTAMP NOT NULL
);
```

## Security

- No authentication required (internal system, no external users).
- Services are only exposed within the Docker Compose network.
- External ports are for development/demo access only.
- Input validation on all endpoints to prevent malformed requests.

## Conventions

| Convention | Standard |
|------------|----------|
| GroupId | `com.micropool` |
| Naming | `shot-service`, `match-service`, `league-service` (hyphenated) |
| Internal port | 8080 for all services |
| Java version | 17 |
| Spring Boot | 4.1.0 |
| Dockerfile pattern | Multi-stage (maven build + jre runtime) or pre-built jar copy |
| Line endings | LF (configure `.gitattributes`) |
| Branch strategy | `dev` for integration, feature branches if needed |

## Failure Handling (Rack 3)

When Shot Service is unavailable:
- Match Service should timeout after a reasonable period (e.g., 3 seconds)
- Return an error response to the caller indicating the shot could not be processed
- **Never partially apply a shot** — match state must not change on failure
- No retry by default (keep it simple); if retries are added, limit to 1–2 attempts
