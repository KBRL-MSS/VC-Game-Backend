package com.kbrl.projects.VC_Game_backend.websocket;

import com.kbrl.projects.VC_Game_backend.models.Party;
import com.kbrl.projects.VC_Game_backend.models.PartyInvite;
import com.kbrl.projects.VC_Game_backend.models.UserSession;
import com.kbrl.projects.VC_Game_backend.service.FriendService;
import com.kbrl.projects.VC_Game_backend.service.PartyService;
import com.kbrl.projects.VC_Game_backend.service.UserSessionService;
import java.security.Principal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WebSocketController {
  @Autowired private SimpMessagingTemplate messagingTemplate;
  @Autowired private UserSessionService userSessionService;
  @Autowired private FriendService friendService;
  @Autowired private PartyService partyService;

  // Status updates
  @MessageMapping("/status")
  public void updateStatus(Principal principal, @Payload StatusUpdateRequest request) {
    String userId = principal.getName();
    UserSession.UserStatus status = request.getStatus();

    userSessionService.updateUserStatus(userId, status);

    // Notify friends of status change
    List<UserSession> onlineFriends = friendService.getOnlineFriends(userId);

    StatusUpdateResponse response = new StatusUpdateResponse(userId, status);
    for (UserSession friend : onlineFriends) {
      messagingTemplate.convertAndSendToUser(friend.getUserId(), "/queue/friend-status", response);
    }
  }

  // Party operations
  @MessageMapping("/party/create")
  public void createParty(Principal principal, @Payload CreatePartyRequest request) {
    String userId = principal.getName();
    Party party = partyService.createParty(userId, request.getName());

    // Notify the creator
    messagingTemplate.convertAndSendToUser(userId, "/queue/party", new PartyResponse(party));
  }

  @MessageMapping("/party/invite")
  public void sendPartyInvite(Principal principal, @Payload PartyInviteRequest request) {
    String senderId = principal.getName();

    // Check if the recipient is online and a friend
    List<UserSession> onlineFriends = friendService.getOnlineFriends(senderId);
    boolean isOnlineFriend =
        onlineFriends
            .stream()
            .anyMatch(friend -> friend.getUserId().equals(request.getReceiverId()));

    if (!isOnlineFriend) {
      messagingTemplate.convertAndSendToUser(
          senderId, "/queue/error", "User is not online or not in your friend list");
      return;
    }

    try {
      PartyInvite invite =
          partyService.inviteToParty(request.getPartyId(), senderId, request.getReceiverId());

      // Notify the invited player
      messagingTemplate.convertAndSendToUser(
          request.getReceiverId(), "/queue/party-invite", new PartyInviteResponse(invite));

      // Confirm to sender
      messagingTemplate.convertAndSendToUser(
          senderId, "/queue/party-invite-sent", "Invite sent to player");
    } catch (Exception e) {
      messagingTemplate.convertAndSendToUser(senderId, "/queue/error", e.getMessage());
    }
  }

  @MessageMapping("/party/accept")
  public void acceptPartyInvite(Principal principal, @Payload InviteActionRequest request) {
    String userId = principal.getName();

    try {
      Party party = partyService.acceptInvite(request.getInviteId(), userId);

      // Notify the user who accepted
      messagingTemplate.convertAndSendToUser(userId, "/queue/party", new PartyResponse(party));

      // Notify all party members about the new member
      for (String memberId : party.getMembers()) {
        if (!memberId.equals(userId)) {
          messagingTemplate.convertAndSendToUser(
              memberId, "/queue/party-update", new PartyResponse(party));
        }
      }
    } catch (Exception e) {
      messagingTemplate.convertAndSendToUser(userId, "/queue/error", e.getMessage());
    }
  }

  @MessageMapping("/party/decline")
  public void declinePartyInvite(Principal principal, @Payload InviteActionRequest request) {
    String userId = principal.getName();

    try {
      partyService.declineInvite(request.getInviteId(), userId);

      // Notify the user
      messagingTemplate.convertAndSendToUser(
          userId, "/queue/party-invite-declined", "Invite declined");
    } catch (Exception e) {
      messagingTemplate.convertAndSendToUser(userId, "/queue/error", e.getMessage());
    }
  }

  @MessageMapping("/party/leave")
  public void leaveParty(Principal principal, @Payload PartyActionRequest request) {
    String userId = principal.getName();

    try {
      Party party = partyService.getParty(request.getPartyId());
      partyService.leaveParty(request.getPartyId(), userId);

      // Notify the user who left
      messagingTemplate.convertAndSendToUser(userId, "/queue/party-left", "You left the party");

      // If party still exists, notify remaining members
      try {
        Party updatedParty = partyService.getParty(request.getPartyId());
        for (String memberId : updatedParty.getMembers()) {
          messagingTemplate.convertAndSendToUser(
              memberId, "/queue/party-update", new PartyResponse(updatedParty));
        }
      } catch (IllegalArgumentException e) {
        // Party was disbanded, no need to notify
      }
    } catch (Exception e) {
      messagingTemplate.convertAndSendToUser(userId, "/queue/error", e.getMessage());
    }
  }

  @MessageMapping("/party/voice/toggle")
  public void toggleVoiceChannel(Principal principal, @Payload VoiceChannelRequest request) {
    String userId = principal.getName();

    try {
      partyService.toggleVoiceChannel(request.getPartyId(), userId, request.isEnabled());

      // Get updated party
      Party party = partyService.getParty(request.getPartyId());

      // Notify all party members
      for (String memberId : party.getMembers()) {
        messagingTemplate.convertAndSendToUser(
            memberId,
            "/queue/party-voice-update",
            new VoiceChannelResponse(party.getId(), party.isVoiceEnabled()));
      }
    } catch (Exception e) {
      messagingTemplate.convertAndSendToUser(userId, "/queue/error", e.getMessage());
    }
  }

  // WebRTC signaling for voice chat
  @MessageMapping("/voice/signal")
  public void handleVoiceSignal(Principal principal, @Payload VoiceSignalRequest request) {
    String senderId = principal.getName();

    // Forward the signal to the target user
    messagingTemplate.convertAndSendToUser(
        request.getTargetUserId(),
        "/queue/voice-signal",
        new VoiceSignalResponse(senderId, request.getSignalData()));
  }
}
