package com.kbrl.projects.VC_Game_backend.models;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class PartyInvite {
  private String id;
  private String partyId;
  private String senderId;
  private String receiverId;
  private LocalDateTime createdAt;
  private LocalDateTime expiresAt;

  public PartyInvite(String partyId, String senderId, String receiverId) {
    this.id = UUID.randomUUID().toString();
    this.partyId = partyId;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.createdAt = LocalDateTime.now();
    // Party invites expire after 5 minutes
    this.expiresAt = this.createdAt.plusMinutes(5);
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }
}
