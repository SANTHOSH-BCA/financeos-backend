package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.service.LiabilityNotificationEventService;
import com.financeos.financeosbackend.liability.service.LiabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/liabilities")
public class LiabilityNotificationEventController {

    private final LiabilityService liabilityService;
    private final LiabilityNotificationEventService eventService;

    public LiabilityNotificationEventController(
            LiabilityService liabilityService,
            LiabilityNotificationEventService eventService
    ) {
        this.liabilityService = liabilityService;
        this.eventService = eventService;
    }

    @PostMapping("/{liabilityId}/notification-events")
    public ResponseEntity<Void> generateNotificationEvents(
            @PathVariable Long liabilityId
    ) {

        /*
         * Event generation is currently exposed only as a development
         * integration point. The actual Notification module will
         * consume LiabilityNotificationEvent through the event layer.
         */

        return ResponseEntity.accepted().build();
    }
}