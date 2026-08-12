package com.micropool.shotservice;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ShotRequest(
    @Min(0) @Max(359) int angle,
    @Min(1) @Max(100) int power
) {}
