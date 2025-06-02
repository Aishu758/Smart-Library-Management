package com.example.authservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.example.authservice.config.FeignClientConfig;
import com.example.authservice.model.Book;

@FeignClient(name = "bookservice", url = "http://localhost:8083", configuration = FeignClientConfig.class)
public interface BookClient {

	 @GetMapping("/api/v1/book/all")  // ✅ correct path
	    List<Book> getAllBooks();
    @GetMapping("/edit/{id}")
    Book getBookById(@PathVariable("id") int id);

    @PostMapping("/add-rest")
    Book addBook(@RequestBody Book book);

    // ✅ REST API call to update book (PUT)
    @PutMapping("/update-rest/{id}")
    Book updateBook(@PathVariable("id") int id, @RequestBody Book book);

    @DeleteMapping("/api/v1/book/{id}")
    void deleteBook(@PathVariable("id") int id);
    @GetMapping("/api/v1/book/books/{id}")
    Book fetchBookById(@PathVariable("id") int id);

}
