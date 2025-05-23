package com.kbrl.projects.VC_Game_backend.dtos;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FriendDto {
  private String id;
  private String username;
  private String displayName;
  private String avatarUrl;
  private LocalDateTime friendSince;
}
