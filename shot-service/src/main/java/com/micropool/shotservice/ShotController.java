package com.micropool.shotservice;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShotController {

    private final ShotCalculator shotCalculator;

    public ShotController(ShotCalculator shotCalculator) {
        this.shotCalculator = shotCalculator;
    }

    @PostMapping("/shots")
    public ResponseEntity<ShotResponse> calculateShot(@Valid @RequestBody ShotRequest request) {
        ShotOutcome outcome = shotCalculator.calculate(request.angle(), request.power());
        ShotResponse response = new ShotResponse(outcome, request.angle(), request.power());
        return ResponseEntity.ok(response);
    }
}
