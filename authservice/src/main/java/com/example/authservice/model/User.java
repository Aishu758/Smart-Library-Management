package com.example.authservice.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private int userid;
	private String username;
	private String password;
	private String email;
	private Long phonenumber;
    private LocalDateTime createdAt;


}
