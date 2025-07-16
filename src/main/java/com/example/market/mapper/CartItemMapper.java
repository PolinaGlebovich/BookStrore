package com.example.market.mapper;

import com.example.market.dto.response.CartItemResponse;
import com.example.market.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    CartItemResponse toDto(CartItem cartItem);
}
