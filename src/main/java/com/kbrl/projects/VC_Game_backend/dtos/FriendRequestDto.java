package com.kbrl.projects.VC_Game_backend.dtos;

import com.kbrl.projects.VC_Game_backend.models.FriendRequest;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FriendRequestDto {
  private String id;
  private String username;
  private String displayName;
  private FriendRequest.RequestStatus status;
  private LocalDateTime createdAt;
}
