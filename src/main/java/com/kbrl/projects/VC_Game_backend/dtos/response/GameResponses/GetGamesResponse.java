package com.kbrl.projects.VC_Game_backend.dtos.response.GameResponses;

import com.kbrl.projects.VC_Game_backend.models.GameType;
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
