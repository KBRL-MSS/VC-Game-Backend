package com.kbrl.projects.VC_Game_backend.dtos;

import lombok.Data;

@Data
public class UserDto {
  private String id;
  private String username;
  private String displayName;
  private String avatarUrl;
}
