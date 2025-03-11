package com.example.market.service;

import com.example.market.dto.BookDto;
import com.example.market.entity.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    Book save(Book book, MultipartFile imageFile) throws IOException;
    Book findByTitle(String title);
    Book findByAuthor(String author);
    Book findByGenre(String genre);
    BookDto findById(Long id) throws IOException;
    void delete (Long id);
    List<BookDto> findAll();
    BookDto updateBook(Long id, BookDto bookDto) throws IOException;
    Book findByImageId(String imageId);
}
