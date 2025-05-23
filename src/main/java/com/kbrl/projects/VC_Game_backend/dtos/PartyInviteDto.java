package com.kbrl.projects.VC_Game_backend.dtos;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PartyInviteDto {
  private String id;
  private String partyId;
  private String partyName;
  private String senderId;
  private String senderName;
  private LocalDateTime createdAt;
  private LocalDateTime expiresAt;
}
