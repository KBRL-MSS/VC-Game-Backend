package com.kbrl.projects.VC_Game_backend.dto.response.partyResponse;

import com.kbrl.projects.VC_Game_backend.model.PartyInvite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyInviteResponse {
    private String id;
    private String partyId;
    private String partyName;
    private String senderId;
    private String senderName;

    public PartyInviteResponse(PartyInvite invite) {
        this.id = invite.getId();
        this.partyId = invite.getPartyId();
        this.senderId = invite.getSenderId();
        // Note: Party name and sender name would need to be looked up
    }
}
