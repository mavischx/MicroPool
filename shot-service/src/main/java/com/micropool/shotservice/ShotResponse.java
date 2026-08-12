package com.micropool.shotservice;

public record ShotResponse(
    ShotOutcome outcome,
    int inputAngle,
    int inputPower
) {}
