package com.kbrl.projects.VC_Game_backend.repository;

import com.kbrl.projects.VC_Game_backend.models.FriendRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
  List<FriendRequest> findBySenderId(String senderId);

  List<FriendRequest> findByReceiverIdAndStatus(
      String receiverId, FriendRequest.RequestStatus status);

  Optional<FriendRequest> findBySenderIdAndReceiverId(String senderId, String receiverId);
}
