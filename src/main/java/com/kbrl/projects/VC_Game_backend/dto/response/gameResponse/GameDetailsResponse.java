package com.kbrl.projects.VC_Game_backend.dto.response.gameResponse;

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
