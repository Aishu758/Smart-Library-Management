package com.example.authservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.authservice.client.BookClient;
import com.example.authservice.client.UserClient;
import com.example.authservice.model.User;

@Controller
@RequestMapping("/")
public class LibraryController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private BookClient bookClient;

    // Home Page
    @GetMapping
    public String showIndexPage() {
        return "index";
    }

    // Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    
    @GetMapping("/loginUser")
    public String showLoginPage2() {
        return "loginuser";
    }

    // Admin Dashboard (uses Feign to load book list)
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("books", bookClient.getAllBooks());
        return "admin"; // Located in authservice/templates/admin.html
    }

    // Redirect to bookservice’s add form page
    @GetMapping("/admin/addBook")
    public String redirectToAddBookForm() {
        return "redirect:http://localhost:8083/api/v1/book/add"; // Renders add.html from bookservice
    }

    // Delete via Feign client
    @PostMapping("/admin/delete/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookClient.deleteBook(id);
        return "redirect:/admin";
    }

    // Optional user page
    @GetMapping("/user")
    public String showUserPage() {
        return "user";
    }
    @PostMapping("/loginuser")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model) {
        Optional<User> optionalUser = userClient.getAllUsers().stream()
            .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
            .findFirst();

        if (optionalUser.isPresent()) {
            model.addAttribute("books", bookClient.getAllBooks()); // optional
            return "user"; // renders user.html from templates
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "loginuser"; // go back to login page
        }
    }

}