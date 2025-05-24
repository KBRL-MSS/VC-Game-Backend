package com.kbrl.projects.VC_Game_backend.websocket;

import com.kbrl.projects.VC_Game_backend.models.Party;
import com.kbrl.projects.VC_Game_backend.models.PartyInvite;
import com.kbrl.projects.VC_Game_backend.models.UserSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Status update
@Data
@NoArgsConstructor
@AllArgsConstructor
class StatusUpdateRequest {
  private UserSession.UserStatus status;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class StatusUpdateResponse {
  private String userId;
  private UserSession.UserStatus status;
}

// Party messages
@Data
@NoArgsConstructor
@AllArgsConstructor
class CreatePartyRequest {
  private String name;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PartyResponse {
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

@Data
@NoArgsConstructor
@AllArgsConstructor
class PartyInviteRequest {
  private String partyId;
  private String receiverId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PartyInviteResponse {
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

@Data
@NoArgsConstructor
@AllArgsConstructor
class InviteActionRequest {
  private String inviteId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PartyActionRequest {
  private String partyId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class VoiceChannelRequest {
  private String partyId;
  private boolean enabled;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class VoiceChannelResponse {
  private String partyId;
  private boolean enabled;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class VoiceSignalRequest {
  private String targetUserId;
  private Object signalData; // WebRTC signaling data
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class VoiceSignalResponse {
  private String fromUserId;
  private Object signalData; // WebRTC signaling data
}
