package com.micropool.league_service.controller;

import com.micropool.league_service.model.LeaderboardEntry;
import com.micropool.league_service.model.MatchResult;
import com.micropool.league_service.repository.MatchResultRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class MatchResultController
{
    private final MatchResultRepository repository;

    public MatchResultController(MatchResultRepository repository)
    {
        this.repository = repository;
    }

    @PostMapping("/results")
    public ResponseEntity<?> saveMatchResult(@RequestBody MatchResult result)
    {
        MatchResult saved = repository.save(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardEntry> getLeaderboard()
    {
        List<MatchResult> allResults = repository.findAll();

        Set<String> players = new HashSet<>();
        for (MatchResult r : allResults)
        {
            players.add(r.getWinner());
            players.add(r.getLoser());
        }

        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        for (String p : players)
        {
            long wins = repository.countByWinner(p);
            long losses = repository.countByLoser(p);
            leaderboard.add(new LeaderboardEntry(p, wins, losses));
        }
        return leaderboard;
    }
}
