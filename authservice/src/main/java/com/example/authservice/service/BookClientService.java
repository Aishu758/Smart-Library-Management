// src/main/java/com/example/authservice/service/BookClientService.java
package com.example.authservice.service;

import com.example.authservice.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookClientService {

    @Autowired
    private RestTemplate restTemplate;

    public Book getBookById(int id) {
        return restTemplate.getForObject("http://BOOKSERVICE/books/" + id, Book.class);
    }
}
