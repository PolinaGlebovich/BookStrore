package com.example.market.mapper;

import com.example.market.dto.request.BookRequest;
import com.example.market.dto.request.UserRequest;
import com.example.market.dto.response.BookResponse;
import com.example.market.dto.response.UserResponse;
import com.example.market.entity.Book;
import com.example.market.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.io.IOException;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    BookResponse toDto(Book book);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Book toEntity(BookRequest bookRequest) throws IOException;

    @Mapping(target = "id", ignore = true)
    void updateBookFromRequest(BookRequest bookRequest, @MappingTarget Book book);

}

