package com.example.market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookResponse {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String author;
    private double price;
}
