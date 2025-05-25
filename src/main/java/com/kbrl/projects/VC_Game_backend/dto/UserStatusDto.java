package com.kbrl.projects.VC_Game_backend.dto;

import com.kbrl.projects.VC_Game_backend.model.UserSession;
import lombok.Data;

@Data
public class UserStatusDto {
  private String userId;
  private String username;
  private String displayName;
  private UserSession.UserStatus status;
  private boolean inParty;
}
