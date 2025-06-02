package com.example.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.authservice.model.User;

@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;
    

    public User getUserByUsername(String username) {
        return restTemplate.getForObject("http://USERSERVICE/users/username/" + username, User.class);
    }
}
