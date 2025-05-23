package com.kbrl.projects.VC_Game_backend.service;

import com.kbrl.projects.VC_Game_backend.models.Friend;
import com.kbrl.projects.VC_Game_backend.models.FriendRequest;
import com.kbrl.projects.VC_Game_backend.models.User;
import com.kbrl.projects.VC_Game_backend.models.UserSession;
import com.kbrl.projects.VC_Game_backend.repository.FriendRepository;
import com.kbrl.projects.VC_Game_backend.repository.FriendRequestRepository;
import com.kbrl.projects.VC_Game_backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {
  private final FriendRepository friendRepository;
  private final FriendRequestRepository friendRequestRepository;
  private final UserRepository userRepository;
  private final UserSessionService userSessionService;

  @Transactional
  public FriendRequest sendFriendRequest(String senderId, String receiverUsername) {
    // Find receiver by username
    User receiver =
        userRepository
            .findByUsername(receiverUsername)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + receiverUsername));

    String receiverId = receiver.getId();

    // Check if users are already friends
    if (friendRepository.existsByUserIdAndFriendId(senderId, receiverId)) {
      throw new IllegalStateException("Users are already friends");
    }

    // Check for existing requests
    if (friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId).isPresent()) {
      throw new IllegalStateException("Friend request already sent");
    }

    if (friendRequestRepository.findBySenderIdAndReceiverId(receiverId, senderId).isPresent()) {
      throw new IllegalStateException("User has already sent you a friend request");
    }

    // Create new request
    FriendRequest request = new FriendRequest();
    request.setSenderId(senderId);
    request.setReceiverId(receiverId);
    request.setStatus(FriendRequest.RequestStatus.PENDING);
    request.setCreatedAt(LocalDateTime.now());
    request.setUpdatedAt(LocalDateTime.now());

    FriendRequest savedRequest = friendRequestRepository.save(request);
    log.info("Friend request sent from {} to {}", senderId, receiverId);

    return savedRequest;
  }

  @Transactional
  public void acceptFriendRequest(String requestId, String userId) {
    FriendRequest request =
        friendRequestRepository
            .findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

    // Verify receiver is the current user
    if (!request.getReceiverId().equals(userId)) {
      throw new IllegalStateException("Not authorized to accept this request");
    }

    // Update request status
    request.setStatus(FriendRequest.RequestStatus.ACCEPTED);
    request.setUpdatedAt(LocalDateTime.now());
    friendRequestRepository.save(request);

    // Create friendship (both ways)
    Friend friendship1 = new Friend();
    friendship1.setUserId(request.getSenderId());
    friendship1.setFriendId(request.getReceiverId());
    friendship1.setFriendsSince(LocalDateTime.now());

    Friend friendship2 = new Friend();
    friendship2.setUserId(request.getReceiverId());
    friendship2.setFriendId(request.getSenderId());
    friendship2.setFriendsSince(LocalDateTime.now());

    friendRepository.save(friendship1);
    friendRepository.save(friendship2);

    log.info(
        "Friend request accepted: {} and {} are now friends",
        request.getSenderId(),
        request.getReceiverId());
  }

  @Transactional
  public void rejectFriendRequest(String requestId, String userId) {
    FriendRequest request =
        friendRequestRepository
            .findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

    // Verify receiver is the current user
    if (!request.getReceiverId().equals(userId)) {
      throw new IllegalStateException("Not authorized to reject this request");
    }

    request.setStatus(FriendRequest.RequestStatus.REJECTED);
    request.setUpdatedAt(LocalDateTime.now());
    friendRequestRepository.save(request);

    log.info("Friend request rejected: {}", requestId);
  }

  public List<FriendRequest> getPendingFriendRequests(String userId) {
    return friendRequestRepository.findByReceiverIdAndStatus(
        userId, FriendRequest.RequestStatus.PENDING);
  }

  public List<Friend> getUserFriends(String userId) {
    return friendRepository.findByUserId(userId);
  }

  public List<UserSession> getOnlineFriends(String userId) {
    List<String> friendIds =
        friendRepository
            .findByUserId(userId)
            .stream()
            .map(Friend::getFriendId)
            .collect(Collectors.toList());

    return userSessionService.getOnlineFriends(friendIds);
  }

  @Transactional
  public void removeFriend(String userId, String friendId) {
    friendRepository
        .findByUserIdAndFriendId(userId, friendId)
        .ifPresent(
            friendship -> {
              // Delete both friendship records
              friendRepository.delete(friendship);
              friendRepository
                  .findByUserIdAndFriendId(friendId, userId)
                  .ifPresent(friendRepository::delete);

              log.info("Friendship removed between {} and {}", userId, friendId);
            });
  }
}
