package com.example.matchservice;

import com.example.matchservice.DTO.CreateMatchRequest;
import com.example.matchservice.DTO.MatchResponse;
import com.example.matchservice.DTO.ShotResult;
import com.example.matchservice.DTO.TakeShotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    @Value("${shot.provider.url}")
    private String shotProviderURL;

    private final MatchService matchService;

    @PostMapping
    public Match createMatch(@RequestBody CreateMatchRequest request) {
        return matchService.createMatch(request);
    }

    @GetMapping("/{id}")
    public Match getMatch(@PathVariable Long id) {
        return matchService.getMatch(id);
    }

    @PostMapping("/{id}/shots")
    public ResponseEntity<?> takeShot(@PathVariable Long id, @RequestBody TakeShotRequest request) {
        // Validate match exists and is in progress BEFORE calling shot service
        Match match = matchService.getMatch(id);
        if (match.getStatus() != Match.Status.IN_PROGRESS) {
            return ResponseEntity.badRequest().body(Map.of("error", "Match is already finished"));
        }

        // Call shot service - if it fails, match state is NOT modified
        ShotResult shotResult;
        try {
            RestClient restClient = RestClient.builder()
                    .baseUrl(shotProviderURL)
                    .build();

            shotResult = restClient.post()
                    .uri("/shots")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(ShotResult.class);
        } catch (Exception e) {
            // RACK 3: Shot service unavailable - return error, do NOT touch match state
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "error", "SHOT_SERVICE_UNAVAILABLE",
                            "message", "Unable to process shot. Shot service is unavailable. Try again later."
                    ));
        }

        if (shotResult == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "error", "SHOT_SERVICE_UNAVAILABLE",
                            "message", "Shot service returned no result."
                    ));
        }

        // Only apply to match state AFTER successful shot service call
        MatchResponse response = matchService.takeShot(id, shotResult);
        return ResponseEntity.ok(response);
    }
}
