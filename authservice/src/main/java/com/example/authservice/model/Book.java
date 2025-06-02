package com.example.authservice.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

	
	private int bookId;
	private String bookName;
	private String author;
	private Date created_at;

}
