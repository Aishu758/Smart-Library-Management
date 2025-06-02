package com.example.userservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;


	@GetMapping
	public List<User> getALLusers() {
		return userService.getALLusers();
	}

	@GetMapping("/{userid}")
	public Optional<User> getById(@PathVariable int userid) {
		return userService.getById(userid);
	}

	@PostMapping
	public User saveUsers(@RequestBody User user) {
		return userService.saveUsers(user);
	}

	@PutMapping("/{userid}")
	public User updateUser(@PathVariable int userid, @RequestBody User user) {
		return userService.updateUsers(userid, user);
	}

	@DeleteMapping("/{userid}")
	public void deleteById(@PathVariable int userid) {
		userService.deleteById(userid);
	}

}
