package com.financeos.financeosbackend.notification.preference;

import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public NotificationPreferenceResponse getOrCreate(
            Long userId
    ) {

        return repository.findByUserId(userId)
                .map(NotificationPreferenceResponse::from)
                .orElseGet(() -> {

                    User user = userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "User not found: " + userId
                                    )
                            );

                    NotificationPreference preference =
                            new NotificationPreference();

                    preference.setUser(user);

                    return NotificationPreferenceResponse.from(
                            repository.save(preference)
                    );
                });
    }
}