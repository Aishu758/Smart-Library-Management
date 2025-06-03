package com.example.authservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.authservice.model.User;

@FeignClient(name = "userservice", url = "http://localhost:8081")  // Assume userservice is running on port 8081
public interface UserClient {

    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable("id") int id);

	@GetMapping("/api/users/username/{username}")
	User getUserByUsername(@PathVariable("username") String username);
	
	 @GetMapping("/api/users/all")
	    List<User> getAllUsers();
	 @GetMapping("/users/{id}")
	    User getUserByIdFromPublicEndpoint(@PathVariable("id") int id); // ✅ Renamed this to avoid conflict
	  @GetMapping("/user/email/{userId}")
	    String getUserEmail(@PathVariable("userId") int userId);

}
