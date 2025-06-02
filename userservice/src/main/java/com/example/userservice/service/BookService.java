package com.example.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userservice.model.Book;
import com.example.userservice.repository.BookRepo;

@Service
public class BookService {
	@Autowired
	private BookRepo repo;

	public List<Book> findAll() {
		return repo.findAll();
	}

	public List<Book> findByBookNameContainingIgnoreCase(String bookName) {
		return repo.findByBookNameContainingIgnoreCase(bookName);
	}

	public Optional<Book> findById(int id) {
		return repo.findById(id);
	}

	public Book save(Book book) {
		return repo.save(book);
	}

	public Book update(int id, Book book) {
		return repo.save(book);
	}

	public void deleteById(int id) {
		repo.deleteById(id);
	}

}
