package com.kbrl.projects.VC_Game_backend.controller;

import com.kbrl.projects.VC_Game_backend.dtos.PartyDto;
import com.kbrl.projects.VC_Game_backend.dtos.PartyInviteDto;
import com.kbrl.projects.VC_Game_backend.models.Party;
import com.kbrl.projects.VC_Game_backend.models.PartyInvite;
import com.kbrl.projects.VC_Game_backend.models.User;
import com.kbrl.projects.VC_Game_backend.repository.UserRepository;
import com.kbrl.projects.VC_Game_backend.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parties")
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<PartyDto>> getUserParties(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Party> parties = partyService.getUserParties(user.getId());
        List<PartyDto> partyDtos = parties.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(partyDtos);
    }

    @GetMapping("/invites")
    public ResponseEntity<List<PartyInviteDto>> getPendingInvites(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<PartyInvite> invites = partyService.getPendingInvites(user.getId());
        List<PartyInviteDto> inviteDtos = invites.stream()
                .map(invite -> {
                    User sender = userRepository.findById(invite.getSenderId())
                            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

                    Party party = partyService.getParty(invite.getPartyId());

                    PartyInviteDto dto = new PartyInviteDto();
                    dto.setId(invite.getId());
                    dto.setPartyId(invite.getPartyId());
                    dto.setPartyName(party.getName());
                    dto.setSenderId(invite.getSenderId());
                    dto.setSenderName(sender.getUsername());
                    dto.setCreatedAt(invite.getCreatedAt());
                    dto.setExpiresAt(invite.getExpiresAt());

                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(inviteDtos);
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<PartyDto> getParty(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String partyId) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Party party = partyService.getParty(partyId);

        // Check if user is in party
        if (!party.getMembers().contains(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        PartyDto partyDto = convertToDto(party);
        return ResponseEntity.ok(partyDto);
    }

    private PartyDto convertToDto(Party party) {
        PartyDto dto = new PartyDto();
        dto.setId(party.getId());
        dto.setName(party.getName());
        dto.setLeaderId(party.getLeaderId());
        dto.setMembers(party.getMembers());
        dto.setCreatedAt(party.getCreatedAt());
        dto.setVoiceEnabled(party.isVoiceEnabled());

        // Optionally look up display names for members
        return dto;
    }
}
