package com.example.market.dto.request;

import com.example.market.entity.CartItem;
import com.example.market.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private User user;
    private Set<CartItem> cartItems;
}
