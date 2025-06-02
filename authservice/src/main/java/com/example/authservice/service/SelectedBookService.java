package com.example.authservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.authservice.client.BookClient;
import com.example.authservice.client.UserClient;
import com.example.authservice.model.Book;
import com.example.authservice.model.SelectedBook;
import com.example.authservice.model.User;

@Service
public class SelectedBookService {

    @Autowired
    private BookClient bookClient;

    @Autowired
    private UserClient userClient;

    /**
     * Returns a list of SelectedBook objects given the selected book IDs and user ID.
     */
    public List<SelectedBook> getSelectedBooks(List<Integer> selectedBookIds, int userId) {
        List<SelectedBook> selectedBooks = new ArrayList<>();

        // Fetch user once (assuming all books belong to same user)
        User user = userClient.getUserById(userId);

        for (Integer bookId : selectedBookIds) {
            // Fetch book info
            Book book = bookClient.getBookById(bookId);

            // Create SelectedBook object with just IDs and important info
            SelectedBook selectedBook = new SelectedBook();
            selectedBook.setUserid(user.getUserid());
            selectedBook.setUserEmail(user.getEmail());
            selectedBook.setBookId(book.getBookId());
            selectedBook.setBookName(book.getBookName());
            selectedBook.setAddedAt(java.time.LocalDateTime.now());

            selectedBooks.add(selectedBook);
        }

        return selectedBooks;
    }
}
