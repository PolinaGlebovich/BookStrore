package com.example.market.mapper;

import com.example.market.dto.BookDto;
import com.example.market.entity.Book;
import java.io.IOException;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-10T16:11:25+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        bookDto.setTitle( book.getTitle() );
        bookDto.setDescription( book.getDescription() );
        bookDto.setGenre( book.getGenre() );
        bookDto.setAuthor( book.getAuthor() );
        bookDto.setPrice( book.getPrice() );
        bookDto.setImageId( book.getImageId() );

        return bookDto;
    }

    @Override
    public Book toEntity(BookDto bookDto) throws IOException {
        if ( bookDto == null ) {
            return null;
        }

        Book book = new Book();

        book.setTitle( bookDto.getTitle() );
        book.setDescription( bookDto.getDescription() );
        book.setGenre( bookDto.getGenre() );
        book.setAuthor( bookDto.getAuthor() );
        book.setPrice( bookDto.getPrice() );
        book.setImageId( bookDto.getImageId() );

        return book;
    }
}
