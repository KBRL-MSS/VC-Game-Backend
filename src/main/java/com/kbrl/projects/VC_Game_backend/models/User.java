package com.kbrl.projects.VC_Game_backend.models;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
  @Id private String id;
  private String username;
  private String email;
  private String password;
  private boolean online = false;

  // Constructors
  public User() {}

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
