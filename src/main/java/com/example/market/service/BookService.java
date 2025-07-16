package com.example.market.service;

import com.example.market.dto.request.BookRequest;
import com.example.market.dto.response.BookResponse;
import com.example.market.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    BookResponse save(Book book, MultipartFile imageFile) throws IOException;
    Page<BookResponse> searchBooks(String title, String author, String genre, Pageable pageable);
    BookResponse findById(Long id);
    void delete(Long id);
    Page<BookResponse> findAll(Pageable pageable);
    BookResponse updateBook(Long id, BookRequest bookRequest);
    BookResponse findByImageId(String imageId);
}