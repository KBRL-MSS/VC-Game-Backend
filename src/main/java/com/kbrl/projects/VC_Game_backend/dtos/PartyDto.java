package com.kbrl.projects.VC_Game_backend.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class PartyDto {
  private String id;
  private String name;
  private String leaderId;
  private Set<String> members;
  private LocalDateTime createdAt;
  private boolean voiceEnabled;
}
