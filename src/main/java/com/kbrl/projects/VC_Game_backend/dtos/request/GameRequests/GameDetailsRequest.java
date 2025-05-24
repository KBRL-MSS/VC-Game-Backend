package com.kbrl.projects.VC_Game_backend.dtos.request.GameRequests;

import com.kbrl.projects.VC_Game_backend.models.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDetailsRequest {
    private GameType gameType;
    private List<String> playerIds;
}
