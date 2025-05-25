package com.kbrl.projects.VC_Game_backend.dto.request.partyRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceChannelRequest {
    private String partyId;
    private boolean enabled;
}
