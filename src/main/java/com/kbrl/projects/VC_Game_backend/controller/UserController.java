package com.kbrl.projects.VC_Game_backend.controller;

import com.kbrl.projects.VC_Game_backend.dtos.UserDto;
import com.kbrl.projects.VC_Game_backend.models.User;
import com.kbrl.projects.VC_Game_backend.repository.UserRepository;
import com.kbrl.projects.VC_Game_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserRepository userRepository;

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    User user =
        userRepository
            .findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());

    return ResponseEntity.ok(userDto);
  }

  @PutMapping("/me")
  public ResponseEntity<UserDto> updateUser(
      @AuthenticationPrincipal UserDetails userDetails, @RequestBody UserDto userDto) {

    String username = userDetails.getUsername();
    UserDto updatedUser = userService.updateUser(username, userDto);
    return ResponseEntity.ok(updatedUser);
  }
}
