package com.financeos.financeosbackend.financialprofile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProtectionContextRequest {

    private String familyResponsibility;

    private String protectionPriority;

    private String protectionPreference;
}