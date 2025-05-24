package com.kbrl.projects.VC_Game_backend.dtos.response.GameResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDetailsResponse {
    private int maxNumOfPlayers;
    private int minNumOfPlayers;
}
