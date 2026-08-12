package com.micropool.league_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntry {
    private String player;
    private long wins;
    private long losses;
}