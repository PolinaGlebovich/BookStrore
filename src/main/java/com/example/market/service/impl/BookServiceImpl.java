package com.example.market.service.impl;

import com.example.market.dto.request.BookRequest;
import com.example.market.dto.request.ImageEvent;
import com.example.market.dto.response.BookResponse;
import com.example.market.entity.Book;
import com.example.market.exception.ImageDeleteFailedException;
import com.example.market.exception.ImageUploadFailedException;
import com.example.market.exception.ResourceNotFoundException;
import com.example.market.mapper.BookMapper;
import com.example.market.repository.BookRepository;
import com.example.market.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = "bookCache")
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public BookResponse save(Book book, MultipartFile imageFile) throws IOException {
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
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    @Cacheable(value = "books", key = "#id", unless = "#result == null")
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("book.not_found", id));
        return bookMapper.toDto(book);
    }

    @Override
    @Cacheable(cacheNames = "books")
    public Page<BookResponse> findAll(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAll(pageable);
        log.info(messageSource.getMessage("book.list.retrieved", null, LocaleContextHolder.getLocale()));
        return booksPage.map(bookMapper::toDto);
    }

    @Override
    @CacheEvict(value = "books", key = "#id")
    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("book.not_found", id));

        if (book.getImageId() != null) {
            try {
                String imageServiceUrl = "http://localhost:8081/api/images/" + book.getImageId();
                restTemplate.delete(imageServiceUrl);
            } catch (Exception e) {
                throw new ImageDeleteFailedException(
                        messageSource.getMessage("image.delete.failed", new Object[]{id}, LocaleContextHolder.getLocale()));
            }
        }

        bookRepository.deleteById(id);
        log.info(messageSource.getMessage("book.deleted", new Object[]{id}, LocaleContextHolder.getLocale()));
    }

    @Override
    @CachePut(value = "books", key = "#book.id")
    @Transactional
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("book.not_found", id));

        bookMapper.updateBookFromRequest(bookRequest, book);

        if (bookRequest.getImageId() != null) {
            book.setImageId(bookRequest.getImageId());
        }

        Book updatedBook = bookRepository.save(book);
        log.info(messageSource.getMessage("book.updated", new Object[]{id}, LocaleContextHolder.getLocale()));
        return bookMapper.toDto(updatedBook);
    }

    @Override
    @Cacheable(value = "booksByImageId", key = "#imageId", unless = "#result == null")
    public BookResponse findByImageId(String imageId) {
        Book book = bookRepository.findBookByImageId(imageId);
        if (book == null) {
            throw new ResourceNotFoundException("book.not_found_by_image", imageId);
        }
        return bookMapper.toDto(book);
    }

    @Override
    @Cacheable(value = "booksSearch", key = "{#title, #author, #genre, #pageable.pageNumber, #pageable.pageSize}")
    public Page<BookResponse> searchBooks(String title, String author, String genre, Pageable pageable) {
        Page<Book> booksPage = bookRepository.searchBooks(title, author, genre, pageable);

        List<BookResponse> bookResponses = booksPage.getContent().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());

        log.info(messageSource.getMessage("book.search.completed", new Object[]{title, author, genre}, LocaleContextHolder.getLocale()));
        return new PageImpl<>(bookResponses, pageable, booksPage.getTotalElements());
    }
}