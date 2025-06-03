package com.ust.emailservice.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.emailservice.dto.EmailRequest;
import com.ust.emailservice.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailSenderService;

    @PostMapping("/due-date")
    public ResponseEntity<String> sendConfirmationEmail(@RequestBody EmailRequest emailRequest) {
        emailSenderService.sendDueDateReminder(emailRequest);
        return ResponseEntity.ok("Reminder email sent to " + emailRequest.getEmail());
    }
}
