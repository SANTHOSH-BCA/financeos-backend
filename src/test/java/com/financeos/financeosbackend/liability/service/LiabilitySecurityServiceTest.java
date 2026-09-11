package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiabilitySecurityServiceTest {

    private final LiabilitySecurityService service =
            new LiabilitySecurityService();

    @Test
    void belongsToUser_ShouldReturnTrue_WhenLiabilityBelongsToUser() {

        User user = new User();
        user.setId(1L);

        Liability liability = new Liability();
        liability.setId(10L);
        liability.setUser(user);

        assertTrue(
                service.belongsToUser(liability, user)
        );
    }

    @Test
    void belongsToUser_ShouldReturnFalse_WhenLiabilityBelongsToAnotherUser() {

        User owner = new User();
        owner.setId(1L);

        User anotherUser = new User();
        anotherUser.setId(2L);

        Liability liability = new Liability();
        liability.setId(10L);
        liability.setUser(owner);

        assertFalse(
                service.belongsToUser(liability, anotherUser)
        );
    }

    @Test
    void belongsToUser_ShouldReturnFalse_WhenLiabilityIsNull() {

        User user = new User();
        user.setId(1L);

        assertFalse(
                service.belongsToUser(null, user)
        );
    }

    @Test
    void belongsToUser_ShouldReturnFalse_WhenUserIsNull() {

        Liability liability = new Liability();
        liability.setId(10L);

        assertFalse(
                service.belongsToUser(liability, null)
        );
    }

    @Test
    void belongsToUser_ShouldReturnFalse_WhenLiabilityHasNoOwner() {

        User user = new User();
        user.setId(1L);

        Liability liability = new Liability();
        liability.setId(10L);

        assertFalse(
                service.belongsToUser(liability, user)
        );
    }

    @Test
    void validateOwnership_ShouldPass_WhenLiabilityBelongsToUser() {

        User user = new User();
        user.setId(1L);

        Liability liability = new Liability();
        liability.setId(10L);
        liability.setUser(user);

        assertDoesNotThrow(() ->
                service.validateOwnership(liability, user)
        );
    }

    @Test
    void validateOwnership_ShouldReject_WhenLiabilityBelongsToAnotherUser() {

        User owner = new User();
        owner.setId(1L);

        User anotherUser = new User();
        anotherUser.setId(2L);

        Liability liability = new Liability();
        liability.setId(10L);
        liability.setUser(owner);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.validateOwnership(
                        liability,
                        anotherUser
                )
        );
    }
}