package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class LiabilitySecurityService {

    public boolean belongsToUser(
            Liability liability,
            User user
    ) {
        if (liability == null || user == null || liability.getUser() == null) {
            return false;
        }

        if (liability.getId() == null || user.getId() == null) {
            return false;
        }

        return liability.getUser().getId().equals(user.getId());
    }

    public void validateOwnership(
            Liability liability,
            User user
    ) {
        if (!belongsToUser(liability, user)) {
            throw new IllegalArgumentException(
                    "Liability does not belong to the current user."
            );
        }
    }
}