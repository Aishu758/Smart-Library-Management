package com.example.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.authservice.config.FeignClientConfig;
import com.example.authservice.dto.EmailRequest;

@FeignClient(name = "emailservice", url = "http://localhost:8088", configuration = FeignClientConfig.class)
public interface EmailClient {

	 	   
	@PostMapping("/api/email/due-date")
	ResponseEntity<String> sendEmail(@RequestBody EmailRequest request);

	 }
