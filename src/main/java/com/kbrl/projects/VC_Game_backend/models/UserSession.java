package com.kbrl.projects.VC_Game_backend.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
  private String userId;
  private String username;
  private UserStatus status;
  private String sessionId;
  private LocalDateTime lastActive;
  private String currentPartyId;

  public enum UserStatus {
    ONLINE,
    OFFLINE
  }
}
