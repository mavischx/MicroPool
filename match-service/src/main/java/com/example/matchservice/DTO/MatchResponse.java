package com.example.matchservice.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchResponse {
//    private boolean foul;
//    private boolean turnContinues;
    private String currentTurn;
    private String matchStatus;
    private int ballsRemaining;
    private boolean eightBallOnTable;


}
