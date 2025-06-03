package com.ust.emailservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ust.emailservice.dto.EmailRequest;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendDueDateReminder(EmailRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your_email@gmail.com"); // optional: set your verified sender email
            message.setTo(request.getEmail());
            message.setSubject("Library Book Due Reminder");

            String body = String.format(
                "Dear %s,\n\nThis is a reminder that the book \"%s\" is due on %s.\n\nPlease return it on time to avoid penalties.\n\nRegards,\nLibrary Team",
                request.getCustomerName(), request.getBookTitle(), request.getDueDate());

            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

	
}