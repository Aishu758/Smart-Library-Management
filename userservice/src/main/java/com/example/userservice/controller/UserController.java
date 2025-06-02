package com.example.userservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;

@Controller
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/api/users/by-username")
    public User getUserByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }


    // REST endpoints

    @GetMapping("/all")
    @ResponseBody
    public List<User> getALLusers() {
        return userService.getALLusers();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Optional<User> getById(@PathVariable int id) {
        return userService.getById(id);
    }

    @PostMapping
    @ResponseBody
    public User saveUsers(@RequestBody User user) {
        return userService.saveUsers(user);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        return userService.updateUsers(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteById(@PathVariable int id) {
        userService.deleteById(id);
    }

    // Thymeleaf view endpoints

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration"; // registration.html Thymeleaf template
    }

    @PostMapping("/reg")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        userService.saveUsers(user);
        model.addAttribute("message", "User registered successfully!");
        return "registration"; // show form again with success message
    }
    
    
  
}
