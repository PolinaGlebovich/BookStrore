package com.example.market.mapper;

import com.example.market.dto.response.CartItemResponse;
import com.example.market.dto.response.CartResponse;
import com.example.market.entity.Cart;
import com.example.market.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CartItemMapper.class})
public interface CartMapper {
    @Mapping(source = "user.id", target = "userId")
    CartResponse toDto(Cart cart);
    List<CartItemResponse> mapCartItems(Set<CartItem> cartItems);
}
