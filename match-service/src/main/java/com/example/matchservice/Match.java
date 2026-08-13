package com.example.matchservice;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Match {

    public enum Status { IN_PROGRESS, FINISHED }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String player1;
    private String player2;
    private String currentTurn;
//    @ElementCollection
//    private Set<Integer> player1Balls;
//    @ElementCollection
//    private Set<Integer> player2Balls;
//    @ElementCollection
//    private Set<Integer> ballsRemaining;
    private int ballsRemaining;
    private boolean eightBallOnTable;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String winner;

}
