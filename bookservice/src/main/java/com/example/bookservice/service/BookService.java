package com.example.bookservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookservice.model.Book;
import com.example.bookservice.repo.BookRepo;

@Service
public class BookService {
	
	@Autowired
	private BookRepo repo;
	
	
	public List<Book> findAll(){
		return repo.findAll();
	}
	
	public List<Book> findByBookNameContainingIgnoreCase(String bookName){
		return repo.findByBookNameContainingIgnoreCase(bookName);
	}
	
	public Optional<Book> findById(int id) {
		return repo.findById(id);
	}
	
	
	
	public Book save(Book book) {
		return repo.save(book);
	}
	
	public Book update(int id, Book book) {
	    Optional<Book> existingBookOpt = repo.findById(id);
	    if (existingBookOpt.isPresent()) {
	        Book existingBook = existingBookOpt.get();
	        existingBook.setBookName(book.getBookName());
	        existingBook.setAuthor(book.getAuthor());
	        // Update other fields if needed
	        return repo.save(existingBook);
	    } else {
	        // If book with id doesn't exist, this saves the incoming book as new
	        return repo.save(book);
	    }
	}

	
	public void deleteById(int id) {
		repo.deleteById(id);
	}

}
