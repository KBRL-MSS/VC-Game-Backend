package com.kbrl.projects.VC_Game_backend.service;

import com.kbrl.projects.VC_Game_backend.models.Party;
import com.kbrl.projects.VC_Game_backend.models.PartyInvite;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyService {
  private final UserSessionService userSessionService;

  private final Map<String, Party> activeParties = new ConcurrentHashMap<>();
  private final Map<String, PartyInvite> pendingInvites = new ConcurrentHashMap<>();

  public Party createParty(String userId, String partyName) {
    if (!userSessionService.isUserOnline(userId)) {
      throw new IllegalStateException("User must be online to create a party");
    }

    Party party = new Party(userId, partyName);
    activeParties.put(party.getId(), party);

    // Update user session to include party
    userSessionService.updateUserParty(userId, party.getId());

    log.info("Party created: {} by user: {}", party.getId(), userId);
    return party;
  }

  public void disbandParty(String partyId, String userId) {
    Party party = getParty(partyId);

    if (!party.getLeaderId().equals(userId)) {
      throw new IllegalStateException("Only the party leader can disband the party");
    }

    // Update all member sessions
    for (String memberId : party.getMembers()) {
      userSessionService.updateUserParty(memberId, null);
    }

    // Remove the party
    activeParties.remove(partyId);
    log.info("Party disbanded: {}", partyId);
  }

  public PartyInvite inviteToParty(String partyId, String senderId, String receiverId) {
    Party party = getParty(partyId);

    // Validation
    if (!party.getMembers().contains(senderId)) {
      throw new IllegalStateException("Sender must be a party member to send invites");
    }

    if (!userSessionService.isUserOnline(receiverId)) {
      throw new IllegalStateException("Can only invite online users");
    }

    if (party.getMembers().contains(receiverId)) {
      throw new IllegalStateException("User is already in the party");
    }

    // Create invite
    PartyInvite invite = new PartyInvite(partyId, senderId, receiverId);
    pendingInvites.put(invite.getId(), invite);

    log.info("Party invite sent: {} to user: {}", invite.getId(), receiverId);
    return invite;
  }

  public Party acceptInvite(String inviteId, String userId) {
    PartyInvite invite = getInvite(inviteId);

    // Validation
    if (!invite.getReceiverId().equals(userId)) {
      throw new IllegalStateException("Only the invited user can accept the invite");
    }

    if (invite.isExpired()) {
      pendingInvites.remove(inviteId);
      throw new IllegalStateException("Invite has expired");
    }

    Party party = getParty(invite.getPartyId());

    // Add user to party
    party.getMembers().add(userId);

    // Update user session
    userSessionService.updateUserParty(userId, party.getId());

    // Remove the invite
    pendingInvites.remove(inviteId);

    log.info("User {} joined party {}", userId, party.getId());
    return party;
  }

  public void declineInvite(String inviteId, String userId) {
    PartyInvite invite = getInvite(inviteId);

    if (!invite.getReceiverId().equals(userId)) {
      throw new IllegalStateException("Only the invited user can decline the invite");
    }

    pendingInvites.remove(inviteId);
    log.info("Party invite declined: {}", inviteId);
  }

  public void leaveParty(String partyId, String userId) {
    Party party = getParty(partyId);

    if (!party.getMembers().contains(userId)) {
      throw new IllegalStateException("User is not in this party");
    }

    // If leader is leaving, assign a new leader or disband party
    if (party.getLeaderId().equals(userId)) {
      if (party.getMembers().size() <= 1) {
        // Last member is leaving, disband party
        activeParties.remove(partyId);
        userSessionService.updateUserParty(userId, null);
        log.info("Party disbanded as last member left: {}", partyId);
        return;
      } else {
        // Assign new leader
        String newLeader =
            party.getMembers().stream().filter(m -> !m.equals(userId)).findFirst().orElseThrow();
        party.setLeaderId(newLeader);
        log.info("New party leader assigned: {} for party {}", newLeader, partyId);
      }
    }

    // Remove user from party
    party.getMembers().remove(userId);
    userSessionService.updateUserParty(userId, null);

    log.info("User {} left party {}", userId, partyId);
  }

  public void toggleVoiceChannel(String partyId, String userId, boolean enabled) {
    Party party = getParty(partyId);

    if (!party.getLeaderId().equals(userId)) {
      throw new IllegalStateException("Only the party leader can toggle voice channels");
    }

    party.setVoiceEnabled(enabled);
    log.info("Voice channel for party {} is now {}", partyId, enabled ? "enabled" : "disabled");
  }

  public List<Party> getUserParties(String userId) {
    return activeParties
        .values()
        .stream()
        .filter(party -> party.getMembers().contains(userId))
        .collect(Collectors.toList());
  }

  public List<PartyInvite> getPendingInvites(String userId) {
    return pendingInvites
        .values()
        .stream()
        .filter(invite -> invite.getReceiverId().equals(userId) && !invite.isExpired())
        .collect(Collectors.toList());
  }

  public Party getParty(String partyId) {
    return Optional.ofNullable(activeParties.get(partyId))
        .orElseThrow(() -> new IllegalArgumentException("Party not found"));
  }

  private PartyInvite getInvite(String inviteId) {
    return Optional.ofNullable(pendingInvites.get(inviteId))
        .orElseThrow(() -> new IllegalArgumentException("Invite not found"));
  }

  @Scheduled(fixedRate = 60000) // Run every minute
  public void cleanupExpiredInvites() {
    List<String> expiredIds = new ArrayList<>();

    LocalDateTime now = LocalDateTime.now();
    pendingInvites.forEach(
        (id, invite) -> {
          if (now.isAfter(invite.getExpiresAt())) {
            expiredIds.add(id);
          }
        });

    expiredIds.forEach(pendingInvites::remove);

    if (!expiredIds.isEmpty()) {
      log.info("Cleaned up {} expired party invites", expiredIds.size());
    }
  }
}
