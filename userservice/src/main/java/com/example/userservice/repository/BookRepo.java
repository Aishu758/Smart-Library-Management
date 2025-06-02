package com.example.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
	 List<Book> findByBookNameContainingIgnoreCase(String bookName);

}
