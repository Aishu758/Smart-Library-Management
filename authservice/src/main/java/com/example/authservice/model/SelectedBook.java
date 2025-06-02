package com.example.authservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userid;           // Just user id from user-service
    private String userEmail;     // Additional info saved here

    private int bookId;           // Just book id from book-service
    private String bookName;      // Additional info saved here

    private LocalDateTime addedAt;
}
