package com.kbrl.projects.VC_Game_backend.dto.response.partyResponse;

import com.kbrl.projects.VC_Game_backend.model.UserSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateResponse {
    private String userId;
    private UserSession.UserStatus status;
}
