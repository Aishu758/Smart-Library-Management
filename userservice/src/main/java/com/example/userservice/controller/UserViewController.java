package com.example.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.userservice.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserViewController {
	

	

	@GetMapping("/view-page")
	public String showUserPage(@RequestParam String username, Model model) {
	    User user = new User();
	    user.setUsername(username);
	    model.addAttribute("user", user);
	    return "user";
	}
//    @GetMapping("/books")
//    public String showUserBooksPage() {
//        return "userbooks"; // corresponds to userbooks.html in templates folder
//    }

	
	
	@GetMapping("/index")
	public String logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate(); // Invalidate user session
	    }
	    return "redirect:http://localhost:8082/index"; // Redirect to login or home page
	}


}
