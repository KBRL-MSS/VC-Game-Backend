package com.kbrl.projects.VC_Game_backend.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Party {
  private String id;
  private String name;
  private String leaderId;
  private Set<String> members;
  private LocalDateTime createdAt;
  private boolean voiceEnabled;

  public Party(String leaderId, String name) {
    this.id = UUID.randomUUID().toString();
    this.leaderId = leaderId;
    this.name = name;
    this.members = new HashSet<>();
    this.members.add(leaderId);
    this.createdAt = LocalDateTime.now();
    this.voiceEnabled = false;
  }
}
