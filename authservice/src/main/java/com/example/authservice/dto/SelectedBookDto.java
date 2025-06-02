package com.example.authservice.dto;

public class SelectedBookDto {
    private String bookName;
    private String userEmail;
    private String addedAtFormatted;

    public SelectedBookDto(String bookName, String userEmail, String addedAtFormatted) {
        this.bookName = bookName;
        this.userEmail = userEmail;
        this.addedAtFormatted = addedAtFormatted;
    }

    // Getters
    public String getBookName() {
        return bookName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getAddedAtFormatted() {
        return addedAtFormatted;
    }
}
