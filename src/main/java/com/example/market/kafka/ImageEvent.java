package com.example.market.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageEvent {

    private String id;

    private String filename;

    private String fileHash;

    private Date uploadDate;

    private Long size;

}