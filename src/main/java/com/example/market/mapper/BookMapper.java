package com.example.market.mapper;

import com.example.market.dto.BookDto;
import com.example.market.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import javax.swing.*;
import java.io.IOException;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    BookDto toDto(Book book);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Book toEntity(BookDto bookDto) throws IOException;

}

