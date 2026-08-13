package com.example.matchservice;

import com.example.matchservice.DTO.CreateMatchRequest;
import com.example.matchservice.DTO.MatchResponse;
import com.example.matchservice.DTO.ShotResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public Match createMatch(CreateMatchRequest request) {
        Match match = new Match();
        match.setPlayer1(request.getPlayer1());
        match.setPlayer2(request.getPlayer2());
        match.setCurrentTurn(request.getPlayer1());
        match.setEightBallOnTable(true);
        match.setStatus(Match.Status.IN_PROGRESS);
        match.setBallsRemaining(14);
        return matchRepository.save(match);
    }

    public Match getMatch(Long id) {
        return matchRepository.findById(id).orElseThrow();
    }

    public MatchResponse takeShot(Long id, ShotResult shotResult) {
        Match match = matchRepository.findById(id).orElseThrow();

        if (match.getStatus() == Match.Status.FINISHED)
            throw new IllegalStateException("Match is already finished");

//        boolean turnContinues = false;


        switch (shotResult.outcome()) {
            case "FOUL" -> switchTurn(match);
            case "MISS" -> switchTurn(match);
            case "POT_ONE" -> match.setBallsRemaining(match.getBallsRemaining() - 1);
            case "POT_TWO" -> match.setBallsRemaining(match.getBallsRemaining() - 2);
        }
//        if (shotResult.eightBallPotted()) {
//            match.setWinner(match.getBallsRemaining() == 0 ? match.getCurrentTurn() : opponent(match));
//            match.setEightBallOnTable(false);
//            match.setStatus(Match.Status.FINISHED);
//        }

        if(match.getBallsRemaining() == 0)
        {
            match.setWinner(match.getCurrentTurn());
            match.setStatus(Match.Status.FINISHED);
            match.setEightBallOnTable(false);
        }

        matchRepository.save(match);
        return toResponse(match);
    }

    private MatchResponse toResponse(Match match) {
        return MatchResponse.builder()
//                .foul(foul)
//                .turnContinues(turnContinues)
                .currentTurn(match.getCurrentTurn())
                .matchStatus(match.getStatus().name())
                .ballsRemaining(match.getBallsRemaining())
                .eightBallOnTable(match.isEightBallOnTable())
                .build();
    }

  public boolean determineWinner(Match match) {
        return match.getStatus() == Match.Status.FINISHED;
    }
    

    private void switchTurn(Match match) {
        match.setCurrentTurn(opponent(match));
    }

    private String opponent(Match match) {
        return match.getCurrentTurn().equals(match.getPlayer1())
                ? match.getPlayer2() : match.getPlayer1();
    }
}
