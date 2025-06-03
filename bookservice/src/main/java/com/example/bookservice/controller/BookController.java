package com.example.bookservice.controller;

import com.example.bookservice.model.Book;
import com.example.bookservice.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/book")
@Slf4j
public class BookController {

    @Autowired
    private BookService service;

    // --- UI Pages ---

    // Form to add a new book (GET)
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add";
    }

    // Handle add book form POST
    @PostMapping("/add")
    public String saveBook(@ModelAttribute Book book, Model model) {
        try {
            service.save(book);
            return "redirect:/api/v1/book/add?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to save book: " + e.getMessage());
            return "add";
        }
    }
    @GetMapping("/books/{id}")
    @ResponseBody
    public ResponseEntity<Book> getBookByIdRest(@PathVariable("id") int id) {
        return service.findById(id)
                .map(book -> ResponseEntity.ok().body(book))
                .orElse(ResponseEntity.notFound().build());
    }

    // Show edit form for a book (GET)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Optional<Book> bookOpt = service.findById(id);
        if (bookOpt.isPresent()) {
            model.addAttribute("book", bookOpt.get());
            return "edit";
        } else {
            return "redirect:/admin?error=BookNotFound";
        }
    }


    // ✅ Handle update book form POST (for Thymeleaf UI)
    @PostMapping("/edit/{id}")
    public String updateBookForm(@PathVariable int id, @ModelAttribute Book book, Model model) {
        try {
            book.setBookId(id);  // ensure id is set
            service.update(id, book);  // update book in DB
            model.addAttribute("book", book);
            model.addAttribute("success", "Book updated successfully!");
            return "redirect:/api/v1/book/edit/" + id + "?success=true";
        } catch (Exception e) {
            model.addAttribute("book", book);
            model.addAttribute("error", "Failed to update book: " + e.getMessage());
            return "edit";
        }
    }



    // ✅ Handle REST API PUT request (for Feign clients, Postman)
//    @PutMapping("/update-rest/{id}")
//    @ResponseBody
//    public ResponseEntity<Book> updateBookRest(@PathVariable int id, @RequestBody Book book) {
//        try {
//            book.setBookId(id);
//            service.update(id, book);
//            return ResponseEntity.ok(book);
//        } catch (Exception e) {
//            log.error("Failed to update book: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    // --- REST API Endpoints ---

    @GetMapping(value = "/all", produces = "application/json")
    @ResponseBody
    public List<Book> getAllBooksJson() {
        return service.findAll();
    }

    @PostMapping("/add-rest")
    @ResponseBody
    public ResponseEntity<Book> addBookRest(@RequestBody Book book) {
        try {
            Book savedBook = service.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (Exception e) {
            log.error("Failed to save book: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete book: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
