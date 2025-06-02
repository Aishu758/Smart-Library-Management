package com.example.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.model.Book;
import com.example.userservice.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookApiController {
	@Autowired
	private BookService bookService;

	@GetMapping
	public List<Book> findAll() {
		return bookService.findAll();
	}

	@PostMapping
	public Book save(@RequestBody Book book) {
		return bookService.save(book);
	}
}
