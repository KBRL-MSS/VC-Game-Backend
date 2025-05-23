package com.kbrl.projects.VC_Game_backend.controller;

import com.kbrl.projects.VC_Game_backend.dtos.FriendDto;
import com.kbrl.projects.VC_Game_backend.dtos.FriendRequestDto;
import com.kbrl.projects.VC_Game_backend.dtos.UserStatusDto;
import com.kbrl.projects.VC_Game_backend.models.Friend;
import com.kbrl.projects.VC_Game_backend.models.FriendRequest;
import com.kbrl.projects.VC_Game_backend.models.User;
import com.kbrl.projects.VC_Game_backend.models.UserSession;
import com.kbrl.projects.VC_Game_backend.repository.UserRepository;
import com.kbrl.projects.VC_Game_backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<FriendDto>> getFriends(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Friend> friends = friendService.getUserFriends(user.getId());
        List<FriendDto> friendDtos = friends.stream()
                .map(friend -> {
                    User friendUser = userRepository.findById(friend.getFriendId())
                            .orElseThrow(() -> new IllegalArgumentException("Friend not found"));

                    FriendDto dto = new FriendDto();
                    dto.setId(friendUser.getId());
                    dto.setUsername(friendUser.getUsername());
                    dto.setFriendSince(friend.getFriendsSince());

                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(friendDtos);
    }

    @GetMapping("/online")
    public ResponseEntity<List<UserStatusDto>> getOnlineFriends(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<UserSession> onlineFriends = friendService.getOnlineFriends(user.getId());
        List<UserStatusDto> statusDtos = onlineFriends.stream()
                .map(session -> {
                    User friendUser = userRepository.findById(session.getUserId())
                            .orElseThrow(() -> new IllegalArgumentException("Friend not found"));

                    UserStatusDto dto = new UserStatusDto();
                    dto.setUserId(friendUser.getId());
                    dto.setUsername(friendUser.getUsername());
                    dto.setStatus(session.getStatus());
                    dto.setInParty(session.getCurrentPartyId() != null);

                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(statusDtos);
    }

    @PostMapping("/request")
    public ResponseEntity<FriendRequestDto> sendFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody FriendRequestDto requestDto) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        FriendRequest request = friendService.sendFriendRequest(user.getId(), requestDto.getUsername());

        FriendRequestDto responseDto = new FriendRequestDto();
        responseDto.setId(request.getId());
        responseDto.setUsername(requestDto.getUsername());
        responseDto.setStatus(request.getStatus());
        responseDto.setCreatedAt(request.getCreatedAt());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDto>> getPendingRequests(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<FriendRequest> requests = friendService.getPendingFriendRequests(user.getId());
        List<FriendRequestDto> requestDtos = requests.stream()
                .map(request -> {
                    User sender = userRepository.findById(request.getSenderId())
                            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

                    FriendRequestDto dto = new FriendRequestDto();
                    dto.setId(request.getId());
                    dto.setUsername(sender.getUsername());
                    dto.setStatus(request.getStatus());
                    dto.setCreatedAt(request.getCreatedAt());

                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestDtos);
    }

    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String requestId) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        friendService.acceptFriendRequest(requestId, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String requestId) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        friendService.rejectFriendRequest(requestId, user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String friendId) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        friendService.removeFriend(user.getId(), friendId);
        return ResponseEntity.ok().build();
    }
}
