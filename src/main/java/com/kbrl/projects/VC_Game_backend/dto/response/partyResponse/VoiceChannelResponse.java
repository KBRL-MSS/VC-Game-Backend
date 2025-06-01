package com.kbrl.projects.VC_Game_backend.dto.response.partyResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceChannelResponse {
    private String partyId;
    private boolean enabled;
}
