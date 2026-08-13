package com.example.matchservice.DTO;

import java.time.Instant;
import java.util.UUID;

public class MatchResult {
    private UUID id;
    private String winner;
    private String loser;
    private Instant endTime;

}
