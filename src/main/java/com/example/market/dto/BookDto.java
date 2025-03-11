package com.example.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookDto {
    private String title;
    private String description;
    private String genre;
    private String author;
    private double price;
    private String imageId;

    // TODO: сделать отдельный дто для ответа
}
