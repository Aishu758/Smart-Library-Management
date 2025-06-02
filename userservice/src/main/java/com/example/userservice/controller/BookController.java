package com.example.userservice.controller;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.userservice.model.Book;
import com.example.userservice.model.SelectedBook;
import com.example.userservice.model.User;
import com.example.userservice.repository.SelectedBookRepository;
import com.example.userservice.service.BookService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookController {
	@Autowired
	private BookService bookService;

	@Autowired
	private SelectedBookRepository selectedBookRepository;

	@GetMapping("/books") // URL to access book list page
	public String showBookList(Model model, @RequestParam(required = false) String search) {
		List<Book> books;
		if (search != null && !search.isEmpty()) {
			books = bookService.findByBookNameContainingIgnoreCase(search);
		} else {
			books = bookService.findAll();
		}
		model.addAttribute("books", books);
		model.addAttribute("search", search);
		return "booklist";
	}

	// Store selected books in session
	@PostMapping("/books/add/{bookId}")
	public String addToSelectedBooks(@PathVariable("bookId") int bookId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Optional<Book> bookOptional = bookService.findById(bookId);
		User user = (User) session.getAttribute("user"); // assumes user is stored in session

		if (bookOptional.isPresent() && user != null) {
			Book book = bookOptional.get();

			boolean alreadySelected = selectedBookRepository.existsByUserAndBook(user, book);
			if (alreadySelected) {
				redirectAttributes.addFlashAttribute("error", "Book already selected!");
			} else {
				SelectedBook selectedBook = new SelectedBook();
				selectedBook.setUser(user);
				selectedBook.setBook(book);
				selectedBookRepository.save(selectedBook);
				redirectAttributes.addFlashAttribute("success", "Book added successfully!");
			}
		}

		return "redirect:/books/selected";
	}

	@GetMapping("/books/selected")
	public String showSelectedBooks(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}

		List<SelectedBook> selectedBooks = selectedBookRepository.findByUser(user);
		model.addAttribute("selectedBooks", selectedBooks);

		return "selectedbooks";
	}

}
