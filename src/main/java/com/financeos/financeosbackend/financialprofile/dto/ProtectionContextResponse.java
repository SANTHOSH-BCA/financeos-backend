package com.financeos.financeosbackend.financialprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProtectionContextResponse {

    private Long id;
    private String familyResponsibility;
    private String protectionPriority;
    private String protectionPreference;
}