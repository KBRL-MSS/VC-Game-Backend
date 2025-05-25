package com.kbrl.projects.VC_Game_backend.dto.request.partyRequest;

import com.kbrl.projects.VC_Game_backend.model.UserSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Status update
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {
    private UserSession.UserStatus status;
}
