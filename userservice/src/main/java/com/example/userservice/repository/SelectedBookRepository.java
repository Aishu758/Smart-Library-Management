package com.example.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.model.Book;
import com.example.userservice.model.SelectedBook;
import com.example.userservice.model.User;

@Repository
public interface SelectedBookRepository extends JpaRepository<SelectedBook, Integer> {
	boolean existsByUserAndBook(User user, Book book);

	List<SelectedBook> findByUser(User user);
}