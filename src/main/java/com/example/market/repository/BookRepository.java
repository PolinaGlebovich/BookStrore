package com.example.market.repository;

import com.example.market.entity.Book;
import com.example.market.repository.specification.BookSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long>,  JpaSpecificationExecutor<Book> {
    default Page<Book> searchBooks(String title, String author, String genre, Pageable pageable) {
        return findAll(Specification.where(BookSpecifications.hasTitle(title))
                .and(BookSpecifications.hasAuthor(author))
                .and(BookSpecifications.hasGenre(genre)), pageable);
    }
    void deleteById(Long id);
    Book findBookByImageId(String imageId);
}
