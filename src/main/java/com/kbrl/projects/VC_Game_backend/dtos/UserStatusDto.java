package com.kbrl.projects.VC_Game_backend.dtos;

import com.kbrl.projects.VC_Game_backend.models.UserSession;
import lombok.Data;

@Data
public class UserStatusDto {
  private String userId;
  private String username;
  private String displayName;
  private UserSession.UserStatus status;
  private boolean inParty;
}
