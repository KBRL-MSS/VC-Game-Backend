package com.kbrl.projects.VC_Game_backend.repository;

import com.kbrl.projects.VC_Game_backend.models.Friend;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FriendRepository extends MongoRepository<Friend, String> {
  List<Friend> findByUserId(String userId);

  Optional<Friend> findByUserIdAndFriendId(String userId, String friendId);

  boolean existsByUserIdAndFriendId(String userId, String friendId);
}
