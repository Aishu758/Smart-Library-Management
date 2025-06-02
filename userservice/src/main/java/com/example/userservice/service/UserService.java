package com.example.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public List<User> getALLusers() {
		return userRepository.findAll();
	}

	public Optional<User> getById(int userid) {
		return userRepository.findById(userid);
	}

	public User saveUsers(User user) {
		return userRepository.save(user);
	}

	public User updateUsers(int userid, User user) {
		return userRepository.save(user);
	}

	public void deleteById(int userid) {
		userRepository.deleteById(userid);
	}

	public Optional<User> findByUsernameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
