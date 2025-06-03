package com.example.authservice.controller;

import com.example.authservice.model.SelectedBook;
import com.example.authservice.service.SelectedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SelectedBookController {

    @Autowired
    private SelectedBookService selectedBookService;

    // Endpoint to view selected books with book IDs
    @GetMapping("/selected-books")
    public String getSelectedBooks(@RequestParam List<Integer> bookIds, 
                                   @RequestParam int userId,
                                   Model model) {
        // Fetch selected books based on book IDs and user ID
        List<SelectedBook> selectedBooks = selectedBookService.getSelectedBooks(bookIds, userId);

        // Add selected books to the model
        model.addAttribute("selectedBooks", selectedBooks);

        // Return the name of the Thymeleaf template (selectedbook.html)
        return "selectedbook";
    }
}
