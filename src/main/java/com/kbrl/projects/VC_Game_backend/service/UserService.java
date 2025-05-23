package com.kbrl.projects.VC_Game_backend.service;

import com.kbrl.projects.VC_Game_backend.dtos.UserDto;
import com.kbrl.projects.VC_Game_backend.models.User;
import com.kbrl.projects.VC_Game_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public UserDto updateUser(String username, UserDto userDto) {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // Update fields (only the ones from DTO you allow users to change)
    if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());

    userRepository.save(user);

    // Map back to DTO
    UserDto updated = new UserDto();
    updated.setId(user.getId());
    updated.setUsername(user.getUsername());
    return updated;
  }
}
