package com.example.userservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @GetMapping("/username/{username}")
    public User getUserByUsername2(@PathVariable String username) {
        return userService.findByUsername(username);
    }
 // In UserController.java of userservice
    @GetMapping("/user/email/{userId}")
    public ResponseEntity<String> getUserEmail(@PathVariable int userId) {
        Optional<User> user = userService.getById(userId);
        return user.map(value -> ResponseEntity.ok(value.getEmail()))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

  
}
