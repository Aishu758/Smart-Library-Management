package com.example.authservice.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.authservice.client.BookClient;
import com.example.authservice.client.EmailClient;
//import com.example.authservice.client.EmailServiceClient;
import com.example.authservice.client.UserClient;
import com.example.authservice.dto.EmailRequest;
import com.example.authservice.dto.SelectedBookDto;
import com.example.authservice.model.Book;
import com.example.authservice.model.SelectedBook;
import com.example.authservice.model.User;
import com.example.authservice.repo.SelectedBookRepository;
import com.example.authservice.service.UserServiceClient;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LibraryController {

    @Autowired
    private EmailClient emailClient;

//    @Autowired
//    private EmailServiceClient emailServiceClient; // Newly added

    @Autowired
    private SelectedBookRepository selectedBookRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private BookClient bookClient;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping("/selectBook/{bookId}")
    public String selectBook(@PathVariable int bookId,
                             Principal principal,
                             HttpSession session,
                             Model model) {

        String userEmail;
        User user;

        // ✅ Prefer session user (custom login)
        user = (User) session.getAttribute("loggedInUser");

        if (user != null) {
            userEmail = user.getEmail();
        } else if (principal != null) {
            // ✅ Fallback to Spring Security login (e.g., admin)
            String username = principal.getName();
            user = userClient.getUserByUsername(username);
            userEmail = (user != null && user.getEmail() != null) ? user.getEmail() : "guest";
        } else {
            userEmail = "guest";
            user = null;
        }

        // ✅ Check if book already selected
        Optional<SelectedBook> existing = selectedBookRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (existing.isPresent()) {
            model.addAttribute("error", "This book is already selected.");
            model.addAttribute("books", bookClient.getAllBooks());
            model.addAttribute("userEmail", userEmail);
            return "userbooks";
        }

        // ✅ Fetch book and save
        Book book = bookClient.fetchBookById(bookId);

        SelectedBook sb = new SelectedBook();
        sb.setUserEmail(userEmail);
        sb.setBookId(book.getBookId());
        sb.setBookName(book.getBookName());
        sb.setAddedAt(LocalDateTime.now());

        selectedBookRepository.save(sb);

        // ✅ Send email
        if (user != null && user.getEmail() != null) {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setCustomerName(user.getUsername());
            emailRequest.setEmail(user.getEmail());
            emailRequest.setBookTitle(book.getBookName());
            emailRequest.setDueDate(LocalDateTime.now().plusDays(7).toString());

            try {
                ResponseEntity<String> response = emailClient.sendEmail(emailRequest);
                System.out.println("Email sent successfully: " + response.getBody());
            } catch (Exception e) {
                System.err.println("Error sending email: " + e.getMessage());
            }
        }

        // ✅ Load selected books
        List<SelectedBook> selectedBooks = selectedBookRepository.findByUserEmail(userEmail);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<SelectedBookDto> dtos = selectedBooks.stream()
                .map(seb -> new SelectedBookDto(
                        seb.getBookName(),
                        seb.getUserEmail(),
                        seb.getAddedAt().format(formatter)
                ))
                .collect(Collectors.toList());

        model.addAttribute("selectedBooks", dtos);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("success", "Book added successfully!");

        return "selectedbook";
    }



    @GetMapping("/selectedBooks")
    public String viewSelectedBooks(Model model) {
        List<SelectedBook> selectedBooks = selectedBookRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<SelectedBookDto> dtoList = selectedBooks.stream()
                .map(b -> new SelectedBookDto(b.getBookName(), b.getUserEmail(), b.getAddedAt().format(formatter)))
                .collect(Collectors.toList());
        model.addAttribute("selectedBooks", dtoList);
        return "selectedbook";
    }

    @GetMapping
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("books", bookClient.getAllBooks());
        return "admin";
    }

    @GetMapping("/admin/addBook")
    public String redirectToAddBookForm() {
        return "redirect:http://localhost:8083/api/v1/book/add";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookClient.deleteBook(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String showUserPage() {
        return "user";
    }

    @GetMapping("/loginuser")
    public String showLoginPage2() {
        return "loginuser";
    }

    @PostMapping("/loginuser")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        Optional<User> optionalUser = userClient.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (optionalUser.isPresent()) {
            // ✅ Save user in session for later use
            session.setAttribute("loggedInUser", optionalUser.get());

            // ✅ Keep your original redirect
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

        model.addAttribute("books", books);
        model.addAttribute("username", username);
        model.addAttribute("search", search);

        return "userbooks";
    }

    // === New method to handle sending email with book details ===
    @PostMapping("/book/selected")
    public String handleSelectedBook(@RequestParam("userId") int userId,
                                     @RequestParam("bookTitle") String bookTitle,
                                     @RequestParam("dueDate") String dueDate,
                                     Model model) {

        User user = userClient.getUserById(userId);

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setCustomerName(user.getUsername());
        emailRequest.setEmail(user.getEmail());
        emailRequest.setBookTitle(bookTitle);
        emailRequest.setDueDate(dueDate);

        try {
            emailClient.sendEmail(emailRequest);
            model.addAttribute("message", "Book added and email sent to: " + user.getEmail());
        } catch (Exception e) {
            model.addAttribute("message", "Book added, but failed to send email.");
            e.printStackTrace();
        }

        return "confirmation";
    }
}
