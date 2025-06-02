package com.example.authservice.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.authservice.client.BookClient;
import com.example.authservice.client.UserClient;
import com.example.authservice.dto.SelectedBookDto;
import com.example.authservice.model.Book;
import com.example.authservice.model.SelectedBook;
import com.example.authservice.model.User;
import com.example.authservice.repo.SelectedBookRepository;
import com.example.authservice.service.UserServiceClient;

@Controller
@RequestMapping("/")
public class LibraryController {

	@Autowired
	private SelectedBookRepository selectedBookRepository;
	@Autowired
	private UserClient userClient;

	@Autowired
	private BookClient bookClient;

	@Autowired
	private UserServiceClient userServiceClient;

	@PostMapping("/selectBook/{bookId}")
	public String selectBook(@PathVariable int bookId, Principal principal, Model model) {

	    // Use a default user email if principal is null
		  String userEmail;
		    if (principal != null) {
		        String username = principal.getName();
		        User user = userClient.getUserByUsername(username);
		        userEmail = (user != null && user.getEmail() != null) ? user.getEmail() : "guest";
		    } else {
		        userEmail = "guest";
		    }

	    // Check if book already selected by this user
	    Optional<SelectedBook> existing = selectedBookRepository.findByUserEmailAndBookId(userEmail, bookId);
	    if (existing.isPresent()) {
	        // Book already selected — show userbooks page with error message
	        model.addAttribute("error", "This book is already selected.");
	        List<Book> books = bookClient.getAllBooks();
	        model.addAttribute("books", books);
	        model.addAttribute("userEmail", userEmail);
	        return "userbooks"; // return to userbooks.html with error
	    }

	    // Book not selected yet — add to selected books
	    Book book = bookClient.fetchBookById(bookId);

	    SelectedBook sb = new SelectedBook();
	    sb.setUserEmail(userEmail);
	    sb.setBookId(book.getBookId());
	    sb.setBookName(book.getBookName());
	    sb.setAddedAt(LocalDateTime.now());

	    selectedBookRepository.save(sb);

	    // Fetch all selected books for this user
	    List<SelectedBook> selectedBooks = selectedBookRepository.findByUserEmail(userEmail);

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    List<SelectedBookDto> dtos = selectedBooks.stream()
	    	    .map(seb -> new SelectedBookDto(
	    	        seb.getBookName(),     // Use seb, the current stream element
	    	        seb.getUserEmail(),
	    	        seb.getAddedAt().format(formatter)
	    	    ))
	    	    .collect(Collectors.toList());


	    // Add to model and show selectedbook.html page
	    model.addAttribute("selectedBooks", dtos);
	    model.addAttribute("userEmail", userEmail);
	    model.addAttribute("success", "Book added successfully!");
	    return "selectedbook";
	}

//	    @GetMapping("/userbooks")
//	    public String showUserBooksPage(Model model, Principal principal, String search) {
//	        String userEmail = (principal != null) ? principal.getName() : "Guest";
//	        List<Book> books = bookClient.getAllBooks();
//
//	        if (search != null && !search.isEmpty()) {
//	            books = books.stream()
//	                    .filter(book -> book.getBookName().toLowerCase().contains(search.toLowerCase())
//	                            || book.getAuthor().toLowerCase().contains(search.toLowerCase()))
//	                    .collect(Collectors.toList());
//	        }
//
//	        model.addAttribute("books", books);
//	        model.addAttribute("userEmail", userEmail); // pass userEmail to the view
//	        model.addAttribute("search", search);
//	        return "userbooks"; // Display the userbooks page
//	    }
//

	@GetMapping("/selectedBooks")
	public String viewSelectedBooks(Model model) {
		// Fetch all selected books
		List<SelectedBook> selectedBooks = selectedBookRepository.findAll();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		List<SelectedBookDto> dtoList = selectedBooks.stream()
				.map(b -> new SelectedBookDto(b.getBookName(), b.getUserEmail(), b.getAddedAt().format(formatter)))
				.collect(Collectors.toList());
		model.addAttribute("selectedBooks", dtoList);
		return "selectedbook";
	}

	// Home Page
	@GetMapping
	public String showIndexPage() {
		return "index";
	}

	// Login Page
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	// Admin Dashboard (uses Feign to load book list)
	@GetMapping("/admin")
	public String showAdminPage(Model model) {
		model.addAttribute("books", bookClient.getAllBooks());
		return "admin"; // Located in authservice/templates/admin.html
	}

	// Redirect to bookservice’s add form page
	@GetMapping("/admin/addBook")
	public String redirectToAddBookForm() {
		return "redirect:http://localhost:8083/api/v1/book/add"; // Renders add.html from bookservice
	}

	// Delete via Feign client
	@PostMapping("/admin/delete/{id}")
	public String deleteBook(@PathVariable("id") int id) {
		bookClient.deleteBook(id);
		return "redirect:/admin";
	}

	// Optional user page
	@GetMapping("/user")
	public String showUserPage() {
		return "user";
	}

	@GetMapping("/loginuser")
	public String showLoginPage2() {
		return "loginuser"; // this is your login user page
	}

	@PostMapping("/loginuser")
	public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
		Optional<User> optionalUser = userClient.getAllUsers().stream()
				.filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst();

		if (optionalUser.isPresent()) {
			model.addAttribute("books", bookClient.getAllBooks());
			// Correct redirect to user view page
			// In loginUser POST
			return "redirect:http://localhost:8081/view-page?username=" + username;
		} else {
			model.addAttribute("error", "Invalid username or password");
			return "loginuser";
		}
	}

	@GetMapping("/api/auth/books")
	@ResponseBody
	public List<Book> getBooksApi() {
		return bookClient.getAllBooks();
	}

	@GetMapping("/userbooks")
	public String showUserBooksPage(Model model, Principal principal, String search) {
		String username = (principal != null) ? principal.getName() : "Guest";
		List<Book> books = bookClient.getAllBooks();

		if (search != null && !search.isEmpty()) {
			books = books.stream()
					.filter(book -> book.getBookName().toLowerCase().contains(search.toLowerCase())
							|| book.getAuthor().toLowerCase().contains(search.toLowerCase()))
					.collect(Collectors.toList());
		}
// Default to "Guest" if no user is logged in
		model.addAttribute("books", books);
		model.addAttribute("username", username); // Add the username (or "Guest") to the model
		model.addAttribute("search", search); // Pass the search term back to the view

		return "userbooks"; // Display the userbooks page
	}

}