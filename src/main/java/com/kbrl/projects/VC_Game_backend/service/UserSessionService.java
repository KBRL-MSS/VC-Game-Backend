package com.kbrl.projects.VC_Game_backend.service;

import com.kbrl.projects.VC_Game_backend.models.User;
import com.kbrl.projects.VC_Game_backend.models.UserSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserSessionService {
  private final Map<String, UserSession> userSessions = new ConcurrentHashMap<>();

  public void addUserSession(User user, String sessionId) {
    UserSession session = new UserSession();
    session.setUserId(user.getId());
    session.setUsername(user.getUsername());
    session.setStatus(UserSession.UserStatus.ONLINE);
    session.setSessionId(sessionId);
    session.setLastActive(LocalDateTime.now());

    userSessions.put(user.getId(), session);
    log.info("User {} is now online", user.getUsername());
  }

  public void removeUserSession(String userId) {
    UserSession removed = userSessions.remove(userId);
    if (removed != null) {
      log.info("User {} is now offline", removed.getUsername());
    }
  }

  public boolean isUserOnline(String userId) {
    return userSessions.containsKey(userId);
  }

  public List<UserSession> getOnlineFriends(List<String> friendIds) {
    return friendIds
        .stream()
        .filter(userSessions::containsKey)
        .map(userSessions::get)
        .collect(Collectors.toList());
  }

  public void updateUserStatus(String userId, UserSession.UserStatus status) {
    UserSession session = userSessions.get(userId);
    if (session != null) {
      session.setStatus(status);
      session.setLastActive(LocalDateTime.now());
    }
  }

  public void updateUserParty(String userId, String partyId) {
    UserSession session = userSessions.get(userId);
    if (session != null) {
      session.setCurrentPartyId(partyId);
    }
  }

  public UserSession getUserSession(String userId) {
    return userSessions.get(userId);
  }

  public List<UserSession> getAllOnlineUsers() {
    return List.copyOf(userSessions.values());
  }
}
