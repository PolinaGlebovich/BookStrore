package com.example.market.repository;

import com.example.market.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);
    Book findByGenre(String genre);
    Book findByAuthor(String author);
    void deleteById(Long id);
    Book findByImageId(String imageId);
}
