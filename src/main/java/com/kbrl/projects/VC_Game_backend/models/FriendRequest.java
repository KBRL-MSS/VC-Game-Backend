package com.kbrl.projects.VC_Game_backend.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "friend_requests")
@CompoundIndex(
    name = "sender_receiver_idx",
    def = "{'senderId': 1, 'receiverId': 1}",
    unique = true)
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {
  @Id private String id;

  private String senderId;
  private String receiverId;
  private RequestStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public enum RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
  }
}
