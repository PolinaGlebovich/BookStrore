package com.example.market.service.impl;

import com.example.market.dto.BookDto;
import com.example.market.entity.Book;
import com.example.market.mapper.BookMapper;
import com.example.market.repository.BookRepository;
import com.example.market.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final RestTemplate restTemplate;

    @Override
    public Book save(Book book, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageServiceUrl = "http://localhost:8081/api/images/upload";
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(imageFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imageFile.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, new HttpHeaders());
            String imageId = restTemplate.postForObject(imageServiceUrl, requestEntity, String.class);

            book.setImageId(imageId);
        }

        log.info("Saving book: {}", book);
        return bookRepository.save(book);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        if (book.getImageId() != null) {
            try {
                String imageServiceUrl = "http://localhost:8081/api/images/" + book.getImageId();
                restTemplate.delete(imageServiceUrl);
            } catch (Exception e) {
                log.error("Failed to delete image for book id: {}", id, e);
            }
        }

        bookRepository.deleteById(id);
        log.info("Deleted book with id: {}", id);
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setGenre(bookDto.getGenre());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());

        if (bookDto.getImageId() != null) {
            book.setImageId(bookDto.getImageId());
        }

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public Book findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public Book findByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public Book findByImageId(String imageId) {
        return bookRepository.findByImageId(imageId);
    }

}
