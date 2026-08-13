package com.example.matchservice;
import com.example.matchservice.DTO.CreateMatchRequest;
import com.example.matchservice.DTO.MatchResponse;
import com.example.matchservice.DTO.ShotResult;
import com.example.matchservice.DTO.TakeShotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    @Value("${shot.provider.url}")
    private String shotProviderURL;
    RestTemplate restTemplate = new RestTemplate();

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
    public MatchResponse takeShot(@PathVariable Long id, @RequestBody TakeShotRequest request) {
        ShotResult shotResult = restTemplate.postForObject(shotProviderURL + "/shots", request, ShotResult.class);
        return matchService.takeShot(id, shotResult);
    }
//TODO add endpoint for receiving results from shot service
//    @PostMapping("/results")
//    public MatchResponse sendResults() {
//        ShotResult shotResult = restTemplate.postForObject(shotProviderURL + "/shots", request, ShotResult.class);
//        return matchService.takeShot(id, shotResult);
//    }


}