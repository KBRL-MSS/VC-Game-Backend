package com.kbrl.projects.VC_Game_backend.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "friends")
@CompoundIndex(name = "user_friend_idx", def = "{'userId': 1, 'friendId': 1}", unique = true)
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
  @Id private String id;

  private String userId;
  private String friendId;
  private LocalDateTime friendsSince;
}
