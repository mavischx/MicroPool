package com.example.matchservice.DTO;


import java.util.List;

public record ShotResult(
        String outcome,
        int inputAngle,
        int inputPower
){}