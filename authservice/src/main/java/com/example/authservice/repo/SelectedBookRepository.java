package com.example.authservice.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.authservice.model.SelectedBook;



@Repository
public interface SelectedBookRepository extends JpaRepository<SelectedBook, Integer> {
    boolean existsByUseridAndBookId(int userid, int bookId);

	List<SelectedBook> findByUserid(int userid);
	
	Optional<SelectedBook> findByUserEmailAndBookId(String userEmail, int bookId);

    List<SelectedBook> findByUserEmail(String userEmail);
}