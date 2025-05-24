package com.kbrl.projects.VC_Game_backend.controller;

import com.kbrl.projects.VC_Game_backend.dtos.request.GameRequests.GameDetailsRequest;
import com.kbrl.projects.VC_Game_backend.dtos.request.GameRequests.GetGamesRequest;
import com.kbrl.projects.VC_Game_backend.dtos.request.GameRequests.StartGameRequest;
import com.kbrl.projects.VC_Game_backend.dtos.response.GameResponses.GameDetailsResponse;
import com.kbrl.projects.VC_Game_backend.dtos.response.GameResponses.GetGamesResponse;
import com.kbrl.projects.VC_Game_backend.dtos.response.GameResponses.StartGameResponse;
import com.kbrl.projects.VC_Game_backend.models.GameType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GameWebSocketController {

    @MessageMapping("/game/get")
    @SendTo("/topic/get-games")
    public GetGamesResponse getGames(GetGamesRequest request) {
        System.out.println("Received request");
        return new GetGamesResponse(List.of(GameType.TIC_TAC_TOE));
    }

    @MessageMapping("/game/details")
    @SendTo("/topic/game-details")
    public GameDetailsResponse getGameDetails(GameDetailsRequest request) {
        if (request.getGameType() == GameType.TIC_TAC_TOE) {
            System.out.println("GameType is TIC_TAC_TOE");
        }
        return new GameDetailsResponse(2, 2);
    }

    @MessageMapping("/game/start-game")
    @SendTo("/topic/start-game")
    public StartGameResponse startGame(StartGameRequest request) {
        return new StartGameResponse();
    }

}
