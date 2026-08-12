package com.micropool.league_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "match_results")
@Getter
@Setter

public class MatchResult {
    @Id
    private UUID id;
    private UUID matchId;
    private String winner;
    private String loser;
    private Instant timestamp;
}
