package com.example.matchservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMatchRequest {
    private String player1;
    private String player2;
}
