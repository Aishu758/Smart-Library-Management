package com.example.userservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserWebController {
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String showHomePage() {
		return "home"; // loads home.html
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user", new User());
		return "login"; // loads login.html
	}

	// Handle login form submission

	@PostMapping("/login")
	public String loginUser(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {
		Optional<User> user = userService.findByUsernameAndPassword(username, password);
		if (user.isPresent()) {
			session.setAttribute("user", user.get()); // ✅ Save user to session
			return "redirect:/books"; // redirect to book list or userhome
		} else {
			model.addAttribute("error", "Invalid username or password");
			return "login";
		}
	}

	// other methods...

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User()); // Bind empty User object to the form
		return "registration"; //
	}

	// Handle form submission
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user) {
		userService.saveUsers(user);
		return "redirect:/register?success"; // Redirect with a success flag
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

	@GetMapping("/view-page")
	public String showUserPage(@RequestParam(required = false) String username, Model model) {
		User user = new User();
		user.setUsername(username != null ? username : "Guest");
		model.addAttribute("user", user);
		return "userhome";
	}


}
