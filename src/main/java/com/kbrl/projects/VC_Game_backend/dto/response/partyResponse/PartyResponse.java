package com.kbrl.projects.VC_Game_backend.dto.response.partyResponse;

import com.kbrl.projects.VC_Game_backend.model.Party;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyResponse {
    private String id;
    private String name;
    private String leaderId;
    private java.util.Set<String> members;
    private boolean voiceEnabled;

    public PartyResponse(Party party) {
        this.id = party.getId();
        this.name = party.getName();
        this.leaderId = party.getLeaderId();
        this.members = party.getMembers();
        this.voiceEnabled = party.isVoiceEnabled();
    }
}
