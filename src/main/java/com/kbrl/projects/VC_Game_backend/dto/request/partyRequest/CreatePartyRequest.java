package com.kbrl.projects.VC_Game_backend.dto.request.partyRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Party messages
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePartyRequest {
    private String name;
}
