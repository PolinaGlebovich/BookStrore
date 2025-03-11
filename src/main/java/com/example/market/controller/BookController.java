package com.example.market.controller;

import com.example.market.dto.BookDto;
import com.example.market.entity.Book;
import com.example.market.mapper.BookMapper;
import com.example.market.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping("/create")
    public Book create(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("genre") String genre,
            @RequestParam("author") String author,
            @RequestParam("price") double price,
            @RequestParam(value = "imageId", required = false) String imageId,
            @RequestParam(value = "file", required = false) MultipartFile imageFile) throws IOException {

        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setDescription(description);
        bookDto.setGenre(genre);
        bookDto.setAuthor(author);
        bookDto.setPrice(price);
        bookDto.setImageId(imageId);

        Book book = bookMapper.toEntity(bookDto);

        return bookService.save(book, imageFile);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<BookDto>> findAll() {
        List<BookDto> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/findByTitle/{title}")
    public ResponseEntity<Book> findBookByTitle(@PathVariable String title) {
        Book book = bookService.findByTitle(title);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/findByAuthor/{author}")
    public ResponseEntity<Book> findBookByAuthor(@PathVariable String author) {
        Book book = bookService.findByAuthor(author);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/findByGenre/{genre}")
    public ResponseEntity<Book> findBookByGenre(@PathVariable String genre) {
        Book book = bookService.findByGenre(genre);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/findBookById/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) throws IOException {
        BookDto bookDto = bookService.findById(id);
        return ResponseEntity.ok(bookDto);
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,
                                              @RequestPart BookDto bookDto,
                                              @RequestPart(value = "file", required = false) MultipartFile imageFile) throws IOException {
        BookDto updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
