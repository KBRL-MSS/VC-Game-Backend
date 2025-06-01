package com.kbrl.projects.VC_Game_backend.dto.response.gameResponse;

import com.kbrl.projects.VC_Game_backend.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetGamesResponse {
    private List<GameType> games;
}
