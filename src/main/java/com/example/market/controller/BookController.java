package com.example.market.controller;

import com.example.market.dto.request.BookRequest;
import com.example.market.dto.response.BookResponse;
import com.example.market.entity.Book;
import com.example.market.exception.ResourceNotFoundException;
import com.example.market.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {

    private final BookService bookService;
    private final MessageSource messageSource;

    @PostMapping("/create")
    @Operation(summary = "Create a new book", description = "Create a new book")
    public ResponseEntity<?> saveBook(
            @RequestPart Book book,
            @RequestPart(name = "file", required = false) MultipartFile image,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws IOException {
        BookResponse savedBook = bookService.save(book, image);
        String message = messageSource.getMessage("book.saved", new Object[]{book.getTitle()}, locale);
        return ResponseEntity.ok(Map.of("message", message, "data", savedBook));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Retrieve a book by its ID")
    public ResponseEntity<?> getBookById(
            @PathVariable Long id,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        BookResponse book = bookService.findById(id);
        String message = messageSource.getMessage("book.found", new Object[]{id}, locale);
        return ResponseEntity.ok(Map.of("message", message, "data", book));
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve a list of all books")
    public ResponseEntity<?> getAllBooks(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        Page<BookResponse> booksPage = bookService.findAll(pageable);
        String message = messageSource.getMessage("book.list.retrieved", null, locale);
        return ResponseEntity.ok(Map.of("message", message, "data", booksPage));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a book by ID", description = "Delete a book by its ID")
    public ResponseEntity<?> deleteBook(
            @PathVariable Long id,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        bookService.delete(id);
        String message = messageSource.getMessage("book.deleted", new Object[]{id}, locale);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a book by ID", description = "Update an existing book by its ID")
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequest bookRequest,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        BookResponse updatedBook = bookService.updateBook(id, bookRequest);
        String message = messageSource.getMessage("book.updated", new Object[]{id}, locale);
        return ResponseEntity.ok(Map.of("message", message, "data", updatedBook));
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books by title, author, or genre with pagination")
    public ResponseEntity<?> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponse> booksPage = bookService.searchBooks(title, author, genre, pageable);
        String message = messageSource.getMessage("book.search.completed", new Object[]{title, author, genre}, locale);
        return ResponseEntity.ok(Map.of("message", message, "data", booksPage));
    }
}