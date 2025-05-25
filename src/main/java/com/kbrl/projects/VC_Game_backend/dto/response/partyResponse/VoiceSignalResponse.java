package com.kbrl.projects.VC_Game_backend.dto.response.partyResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceSignalResponse {
  private String fromUserId;
  private Object signalData; // WebRTC signaling data
}
